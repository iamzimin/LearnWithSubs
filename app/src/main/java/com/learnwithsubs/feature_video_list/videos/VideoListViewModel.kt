package com.learnwithsubs.feature_video_list.videos

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.learnwithsubs.feature_video_list.VideoConstants
import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.models.VideoErrorType
import com.learnwithsubs.feature_video_list.models.VideoLoadingType
import com.learnwithsubs.feature_video_list.models.VideoStatus
import com.learnwithsubs.feature_video_list.repository.VideoTranscodeRepository
import com.learnwithsubs.feature_video_list.usecase.VideoListUseCases
import com.learnwithsubs.feature_video_list.util.OrderType
import com.learnwithsubs.feature_video_list.util.VideoOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.withContext
import java.io.File
import java.util.LinkedList
import javax.inject.Inject


class VideoListViewModel @Inject constructor(
    val videoListUseCases: VideoListUseCases,
    val videoTranscodeRepository: VideoTranscodeRepository
) : ViewModel() {

    val videoList = MediatorLiveData<List<Video>?>()
    var sortMode: MutableLiveData<VideoOrder> = MutableLiveData<VideoOrder>().apply { value = DEFAULT_SORT_MODE }
    var filter: String? = null
    var editableVideo: Video? = null

    val videoToUpdate = MutableLiveData<Video>()
    val videoProgressLiveData: MutableLiveData<Video?> = videoTranscodeRepository.getVideoProgressLiveData()
    val errorTypeLiveData = MutableLiveData<Video>()

    private val videoSemaphore = Semaphore(1)
    private val processQueue = LinkedList<Video?>()

    companion object {
        val DEFAULT_SORT_MODE: VideoOrder = VideoOrder.Date(OrderType.Descending)
    }

    init {
        updateVideoList(videoOrder = sortMode.value, filter = filter)
    }

    fun onEvent(event: VideosEvent) {
        when (event) {
            is VideosEvent.UpdateVideoList -> updateVideoList(videoOrder = sortMode.value, filter = filter)
            is VideosEvent.Filter -> {
                setFilterMode(filter = event.filter)
                updateVideoList(videoOrder = sortMode.value, filter = filter)
            }
            is VideosEvent.SetOrderMode -> setOrderMode(orderMode = event.orderMode)
            is VideosEvent.DeSelect -> deSelectVideo(isNeedSelect = event.isNeedSelect)
            is VideosEvent.DeleteVideo -> deleteVideo(video = event.video)
            is VideosEvent.DeleteSelectedVideos -> deleteSelectedVideo(selectedVideos = event.videos)
            is VideosEvent.LoadVideo -> addVideo(event.video)
            is VideosEvent.UpdateVideo -> editVideo(event.video)
            is VideosEvent.RenameVideo -> renameVideo(event.video)
        }
    }

    private fun updateVideoList(videoOrder: VideoOrder?, filter: String?) {
        videoList.addSource(
            videoListUseCases.getVideoListUseCase.invoke(
                videoOrder = videoOrder ?: DEFAULT_SORT_MODE,
                filter = filter
            ).asLiveData()
        ) { list ->
            videoList.value = ArrayList(list)
        }
    }

    private fun editVideo(video: Video) {
        viewModelScope.launch { videoListUseCases.loadVideoUseCase.invoke(video) }
    }

    private fun addVideo(video: Video) {
       viewModelScope.launch {
            withContext(Dispatchers.IO) {
                videoListUseCases.loadVideoUseCase.invoke(video)
                val lastVideo: Video? = videoListUseCases.getLastVideoUseCase.invoke()
                processQueue.add(lastVideo)

                videoSemaphore.acquire()
                try {
                    val poolList = processQueue.poll() ?: return@withContext

                    // Обработка извлечение аудио
                    poolList.loadingType = VideoLoadingType.EXTRACTING_AUDIO
                    videoListUseCases.loadVideoUseCase.invoke(poolList)
                    val extractedAudio: Video = videoListUseCases.extractAudioUseCase.invoke(poolList) ?: return@withContext // Return, если null (пользователь отменил загрузку)
                    if (extractedAudio.errorType != null) { // Если ошибка не пуста, то отправка ошибки + остановка обработки
                        errorTypeLiveData.postValue(extractedAudio)
                        return@withContext
                    }

                    // Декодирование видео
                    val transcodeVideo = async {
                        poolList.loadingType = VideoLoadingType.DECODING_VIDEO
                        videoListUseCases.loadVideoUseCase.invoke(poolList)
                        return@async videoListUseCases.transcodeVideoUseCase.invoke(poolList)
                    }

                    // Загрузка аудио
                    val subtitlesFromServer = async {
                       return@async videoListUseCases.getSubtitlesFromServerUseCase.invoke(extractedAudio)
                    }

                    // Ожидание декодирования видео
                    val recodedVideo = transcodeVideo.await() ?: return@withContext // Return, если null (пользователь отменил загрузку)
                    if (recodedVideo.errorType != null) { // Если ошибка не пуста, то отправка ошибки + остановка обработки
                        errorTypeLiveData.postValue(recodedVideo)
                        return@withContext
                    }

                    // После выполненеия декодирования ставится стстус генерации субтитров, если они ещё не готовы
                    if (subtitlesFromServer.isActive) {
                        poolList.loadingType = VideoLoadingType.GENERATING_SUBTITLES
                        videoListUseCases.loadVideoUseCase.invoke(poolList)
                    }
                    val videoSubtitles = subtitlesFromServer.await()
                    if (videoSubtitles.errorType != null) {
                        errorTypeLiveData.postValue(videoSubtitles)
                        return@withContext
                    }

                    // Генерация превью
                    videoListUseCases.extractVideoPreviewUseCase.invoke(recodedVideo)

                    // Успешное завершение
                    recodedVideo.videoStatus = VideoStatus.NORMAL_VIDEO
                    videoListUseCases.loadVideoUseCase.invoke(recodedVideo)
                } finally {
                    videoSemaphore.release()
                }
            }
        }
    }

    // Функиця проверки+возврата типа ошибки у видео. Если ошибки нет - null
//    private fun checkVideoError(video: Video): VideoErrorType? {
//        return when(video.errorType) {
//            VideoErrorType.EXTRACTING_AUDIO -> VideoErrorType.EXTRACTING_AUDIO
//            VideoErrorType.DECODING_VIDEO -> VideoErrorType.DECODING_VIDEO
//            VideoErrorType.GENERATING_SUBTITLES -> VideoErrorType.GENERATING_SUBTITLES
//            VideoErrorType.LOADING_AUDIO -> VideoErrorType.LOADING_AUDIO
//            null -> null
//        }
//    }

    private fun deSelectVideo(isNeedSelect: Boolean) {
        val copiedVideoList = videoList.value?.map { video -> video.copy(isSelected = isNeedSelect) }
        videoList.value = copiedVideoList?.toMutableList()
    }

    private fun deleteSelectedVideo(selectedVideos: List<Video>?) {
        viewModelScope.launch {
            selectedVideos?.forEach { deleteVideo(it)}
        }
    }

    private fun deleteVideo(video: Video) {
        viewModelScope.launch {
            videoListUseCases.deleteVideoUseCase.invoke(video)
        }
        val subSTR = File(video.outputPath)
        if (subSTR.exists())
            subSTR.deleteRecursively()
    }

    private fun renameVideo(video: Video) {
        viewModelScope.launch {
            videoListUseCases.loadVideoUseCase.invoke(video = video)
        }
    }

    private fun setOrderMode(orderMode: VideoOrder) {
        sortMode.value = orderMode
    }
    private fun setFilterMode(filter: String?) {
        this.filter = filter
    }

}