package com.learnwithsubs.video_list.presentation.videos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.base.util.OrderType
import com.example.base.util.VideoOrder
import com.example.video_transcode.domain.repository.VideoTranscodeRepository
import com.learnwithsubs.video_list.domain.models.Video
import com.learnwithsubs.video_list.domain.models.VideoLoadingType
import com.learnwithsubs.video_list.domain.models.VideoStatus
import com.learnwithsubs.video_list.domain.usecase.VideoListUseCases
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
) : ViewModel() {
    companion object {
        val DEFAULT_SORT_MODE: VideoOrder = VideoOrder.Date(OrderType.Descending)
    }

    val videoList = videoListUseCases.getVideoListUseCase.invoke().asLiveData(viewModelScope.coroutineContext)

    var videoOrder: MutableLiveData<VideoOrder> = MutableLiveData<VideoOrder>().apply { value = DEFAULT_SORT_MODE }
    private var filter: String? = null
    var editableVideo: Video? = null

    val videoProgressLiveData: MutableLiveData<Video?> = videoListUseCases.getVideoProgressLiveData.invoke()
    val errorTypeLiveData = MutableLiveData<Video?>()

    private val videoSemaphore = Semaphore(1)
    private val processQueue = LinkedList<Video?>()
    private lateinit var poolList: Video



    fun getSortedVideoList(videoList: List<Video>): List<Video> {
        val sort = getVideoOrder()
        return videoListUseCases.sortVideoListUseCase(videoList = ArrayList(videoList), sortMode = sort, filter = filter)
    }

    fun editVideo(video: Video) {
        viewModelScope.launch { videoListUseCases.loadVideoUseCase.invoke(video) }
    }

    fun addVideo(video: Video) {
        if (video.errorType != null) {
            errorTypeLiveData.postValue(video)
            return
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                videoListUseCases.loadVideoUseCase.invoke(video)
                val lastVideo: Video? = videoListUseCases.getLastVideoUseCase.invoke()
                processQueue.add(lastVideo)

                videoSemaphore.acquire()
                try {
                    poolList = processQueue.poll() ?: return@withContext

                    // Обработка извлечение аудио
                    poolList.loadingType = VideoLoadingType.EXTRACTING_AUDIO
                    editVideo(poolList)
                    // TODO Return, если null (пользователь отменил загрузку)
                    val extractedAudio: Video = videoListUseCases.extractAudioUseCase.invoke(poolList) ?: return@withContext
                    // Если ошибка не пуста, то отправка ошибки + остановка обработки
                    if (extractedAudio.errorType != null) {
                        errorTypeLiveData.postValue(extractedAudio)
                        return@withContext
                    }

                    // Декодирование видео
                    val transcodeVideo = async {
                        poolList.loadingType = VideoLoadingType.DECODING_VIDEO
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
                        poolList.loadingType = VideoLoadingType.GENERATING_SUBTITLES
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
                    recodedVideo.videoStatus = VideoStatus.NORMAL_VIDEO
                    recodedVideo.loadingType = VideoLoadingType.DONE
                    editVideo(recodedVideo)
                } finally {
                    videoSemaphore.release()
                }
            }
        }
    }

    fun deleteSelectedVideo(selectedVideos: List<Video>?) {
        viewModelScope.launch {
            selectedVideos?.forEach { deleteVideo(it)}
        }
    }

    fun deleteVideo(video: Video) {
        viewModelScope.launch {
            videoListUseCases.deleteVideoUseCase.invoke(video)
        }
        if (::poolList.isInitialized && poolList.id == video.id) {
            videoListUseCases.cancelTranscodeVideo.invoke()
            videoListUseCases.cancelExtractAudio.invoke()
        } else {
            processQueue.remove(processQueue.find { it?.id == video.id })
        }
    }

    fun loadNewSubtitles(video: Video, subtitles: String) {
        viewModelScope.launch {
            videoListUseCases.loadNewSubtitlesUseCase.invoke(video = video, subtitles = subtitles)
        }
    }
    fun backOldSubtitles(video: Video) {
        viewModelScope.launch {
            videoListUseCases.backOldSubtitlesUseCase.invoke(video = video)
        }
    }


    fun setVideoOrder(orderMode: VideoOrder) {
        videoOrder.value = orderMode
    }
    fun getVideoOrder(): VideoOrder {
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