package com.learnwithsubs.feature_video_list.presentation.videos

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.models.VideoLoadingType
import com.learnwithsubs.feature_video_list.domain.repository.VideoTranscodeRepository
import com.learnwithsubs.feature_video_list.domain.usecase.VideoListUseCases
import com.learnwithsubs.feature_video_list.domain.util.OrderType
import com.learnwithsubs.feature_video_list.domain.util.VideoOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.withContext
import java.util.LinkedList
import javax.inject.Inject


class VideoListViewModel @Inject constructor(
    val videoListUseCases: VideoListUseCases,
    val videoTranscodeRepository: VideoTranscodeRepository
) : ViewModel() {

    val videoList = MediatorLiveData<List<Video>?>()
    var sortMode: MutableLiveData<VideoOrder> = MutableLiveData<VideoOrder>().apply {
        value = DEFAULT_SORT_MODE
    }

    val videoToUpdate = MutableLiveData<Video>()
    val videoProgressLiveData: MutableLiveData<Video?> = videoTranscodeRepository.getVideoProgressLiveData()

    private val videoSemaphore = Semaphore(1)
    private val processQueue = LinkedList<Video?>()

    companion object {
        val DEFAULT_SORT_MODE: VideoOrder = VideoOrder.Date(OrderType.Descending)
    }

    init {
        updateVideoList()
    }
    fun updateVideoList() {
        videoList.addSource(
            videoListUseCases.getVideoListUseCase.invoke(
                videoOrder = sortMode.value ?: DEFAULT_SORT_MODE
            ).asLiveData()
        ) { list ->
            videoList.value = ArrayList(list)
        }
    }

    fun onEvent(event: VideosEvent) {
        when (event) {
            is VideosEvent.Order -> {
                //if (false) TODO add a check for the same choice
                //return getVideos(videoOrder = event.videoOrder)
            }
            is VideosEvent.DeleteVideo -> {
                viewModelScope.launch {
                    videoListUseCases.deleteVideoUseCase.invoke(event.video)
                }
            }
            is VideosEvent.LoadVideo -> {
                addVideo(event.video)
            }
            is VideosEvent.UpdateVideo -> {
                editVideo(event.video)
            }
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
                try { //TODO обработать возможный null
                    val poolList = processQueue.poll() ?: return@withContext

                    // Обработка извлечение аудио
                    poolList.loadingType = VideoLoadingType.EXTRACTING_AUDIO
                    //videoToUpdate.postValue(poolList)
                    videoListUseCases.loadVideoUseCase.invoke(poolList)
                    val extractedAudio: Video? = videoListUseCases.extractAudioUseCase.invoke(poolList)

                    // Загрузка аудио
                    poolList.loadingType = VideoLoadingType.GENERATING_SUBTITLES
                    //videoToUpdate.postValue(poolList)
                    videoListUseCases.loadVideoUseCase.invoke(poolList)
                    videoListUseCases.sendAudioToServerUseCase.invoke(extractedAudio)

                    // Декодирование видео
                    poolList.loadingType = VideoLoadingType.DECODING_VIDEO
                    //videoToUpdate.postValue(poolList)
                    videoListUseCases.loadVideoUseCase.invoke(poolList)
                    val recodedVideo: Video? = videoListUseCases.transcodeVideoUseCase.invoke(poolList)

                    // Успешное завершение
                    recodedVideo?.let { videoListUseCases.loadVideoUseCase.invoke(it) }
                } finally {
                    videoSemaphore.release()
                }
            }
        }
    }

    fun deSelectVideo(isNeedSelect: Boolean) {
        val copiedVideoList = videoList.value?.map { video -> video.copy(isSelected = isNeedSelect) }
        videoList.value = copiedVideoList?.toMutableList()
    }

    fun deleteSelectedVideo() {
        val videoToDelete = videoList.value?.filter { it.isSelected }
        viewModelScope.launch {
            videoToDelete?.forEach { videoListUseCases.deleteVideoUseCase.invoke(it)}
        }
    }

    fun setSortMode(orderMode: VideoOrder) {
        sortMode.value = orderMode
    }

}