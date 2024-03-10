package com.learnwithsubs.video_list.presentation.videos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.learnwithsubs.general.util.OrderType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.withContext
import java.io.File
import java.util.LinkedList
import javax.inject.Inject


class VideoListViewModel @Inject constructor(
    val videoListUseCases: com.learnwithsubs.video_list.domain.usecase.VideoListUseCases,
    val videoTranscodeRepository: com.learnwithsubs.video_list.domain.repository.VideoTranscodeRepository
) : ViewModel() {

    //private val videoListFlow: Flow<List<Video>> = videoListUseCases.getVideoListUseCase.invoke()
    val videoList = videoListUseCases.getVideoListUseCase.invoke().asLiveData(viewModelScope.coroutineContext)

    var videoOrder: MutableLiveData<com.learnwithsubs.video_list.domain.util.VideoOrder> = MutableLiveData<com.learnwithsubs.video_list.domain.util.VideoOrder>().apply { value = DEFAULT_SORT_MODE }
    private var filter: String? = null
    var editableVideo: com.learnwithsubs.video_list.domain.models.Video? = null

    val videoProgressLiveData: MutableLiveData<com.learnwithsubs.video_list.domain.models.Video?> = videoTranscodeRepository.getVideoProgressLiveData()
    val errorTypeLiveData = MutableLiveData<com.learnwithsubs.video_list.domain.models.Video?>()

    private val videoSemaphore = Semaphore(1)
    private val processQueue = LinkedList<com.learnwithsubs.video_list.domain.models.Video?>()
    private lateinit var poolList: com.learnwithsubs.video_list.domain.models.Video

    companion object {
        val DEFAULT_SORT_MODE: com.learnwithsubs.video_list.domain.util.VideoOrder = com.learnwithsubs.video_list.domain.util.VideoOrder.Date(OrderType.Descending)
    }

    fun getSortedVideoList(videoList: List<com.learnwithsubs.video_list.domain.models.Video>): List<com.learnwithsubs.video_list.domain.models.Video> {
        val sort = getVideoOrder()
        return videoListUseCases.sortVideoListUseCase(videoList = ArrayList(videoList), sortMode = sort, filter = filter)
    }

    fun editVideo(video: com.learnwithsubs.video_list.domain.models.Video) {
        viewModelScope.launch { videoListUseCases.loadVideoUseCase.invoke(video) }
    }

    fun addVideo(video: com.learnwithsubs.video_list.domain.models.Video) {
        if (video.errorType != null) {
            errorTypeLiveData.postValue(video)
            return
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                videoListUseCases.loadVideoUseCase.invoke(video)
                val lastVideo: com.learnwithsubs.video_list.domain.models.Video? = videoListUseCases.getLastVideoUseCase.invoke()
                processQueue.add(lastVideo)

                videoSemaphore.acquire()
                try {
                    poolList = processQueue.poll() ?: return@withContext

                    // Обработка извлечение аудио
                    poolList.loadingType = com.learnwithsubs.video_list.domain.models.VideoLoadingType.EXTRACTING_AUDIO
                    editVideo(poolList)
                    // TODO Return, если null (пользователь отменил загрузку)
                    val extractedAudio: com.learnwithsubs.video_list.domain.models.Video = videoListUseCases.extractAudioUseCase.invoke(poolList) ?: return@withContext
                    // Если ошибка не пуста, то отправка ошибки + остановка обработки
                    if (extractedAudio.errorType != null) {
                        errorTypeLiveData.postValue(extractedAudio)
                        return@withContext
                    }

                    // Декодирование видео
                    val transcodeVideo = async {
                        poolList.loadingType = com.learnwithsubs.video_list.domain.models.VideoLoadingType.DECODING_VIDEO
                        editVideo(poolList)
                        return@async videoListUseCases.transcodeVideoUseCase.invoke(poolList)
                    }

                    // Загрузка аудио и сохранение субтитров
                    val subtitlesFromServer = async {
                       return@async videoListUseCases.getSubtitlesFromServerUseCase.invoke(extractedAudio)
                    }

                    // Ожидание декодирования видео
                    // Return, если null (пользователь отменил загрузку)
                    val recodedVideo = transcodeVideo.await() ?: return@withContext
                    // Если ошибка не пуста, то отправка ошибки + остановка обработки
                    if (recodedVideo.errorType != null) {
                        errorTypeLiveData.postValue(recodedVideo)
                        return@withContext
                    }

                    // После выполненеия декодирования ставится стстус генерации субтитров, если они ещё не готовы
                    if (subtitlesFromServer.isActive) {
                        poolList.loadingType = com.learnwithsubs.video_list.domain.models.VideoLoadingType.GENERATING_SUBTITLES
                        editVideo(poolList)
                    }
                    val videoSubtitles = subtitlesFromServer.await()
                    if (videoSubtitles.errorType != null) {
                        errorTypeLiveData.postValue(videoSubtitles)
                        return@withContext
                    }

                    // Генерация превью
                    videoListUseCases.extractVideoPreviewUseCase.invoke(recodedVideo)

                    // Успешное завершение
                    recodedVideo.videoStatus = com.learnwithsubs.video_list.domain.models.VideoStatus.NORMAL_VIDEO
                    recodedVideo.loadingType = com.learnwithsubs.video_list.domain.models.VideoLoadingType.DONE
                    editVideo(recodedVideo)
                } finally {
                    videoSemaphore.release()
                }
            }
        }
    }

    fun deleteSelectedVideo(selectedVideos: List<com.learnwithsubs.video_list.domain.models.Video>?) {
        viewModelScope.launch {
            selectedVideos?.forEach { deleteVideo(it)}
        }
    }

    fun deleteVideo(video: com.learnwithsubs.video_list.domain.models.Video) {
        viewModelScope.launch {
            videoListUseCases.deleteVideoUseCase.invoke(video)
        }
        if (::poolList.isInitialized && poolList.id == video.id) {
            videoTranscodeRepository.cancelTranscodeVideo()
            videoTranscodeRepository.cancelExtractAudio()
        } else {
            processQueue.remove(processQueue.find { it?.id == video.id })
        }
        val subSTR = File(video.outputPath)
        if (subSTR.exists())
            subSTR.deleteRecursively()
    }

    fun loadNewSubtitles(video: com.learnwithsubs.video_list.domain.models.Video, subtitles: String) {
        viewModelScope.launch {
            videoListUseCases.loadNewSubtitlesUseCase.invoke(video = video, subtitles = subtitles)
        }
    }
    fun backOldSubtitles(video: com.learnwithsubs.video_list.domain.models.Video) {
        viewModelScope.launch {
            videoListUseCases.backOldSubtitlesUseCase.invoke(video = video)
        }
    }


    fun setVideoOrder(orderMode: com.learnwithsubs.video_list.domain.util.VideoOrder) {
        videoOrder.value = orderMode
    }
    fun getVideoOrder(): com.learnwithsubs.video_list.domain.util.VideoOrder {
        return videoOrder.value ?: DEFAULT_SORT_MODE
    }

    fun setOrderType(newOrderType: OrderType) {
        videoOrder.value?.apply { orderType = newOrderType } ?: DEFAULT_SORT_MODE
    }
    fun getOrderType(): OrderType {
        return videoOrder.value?.orderType ?: DEFAULT_SORT_MODE.orderType
    }

    fun setFilterMode(filter: String?) {
        this.filter = filter
    }
}