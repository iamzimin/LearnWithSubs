package com.learnwithsubs.feature_video_list.presentation.videos

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.models.VideoLoadingType
import com.learnwithsubs.feature_video_list.domain.repository.VideoTranscodeRepository
import com.learnwithsubs.feature_video_list.domain.usecase.VideoListUseCases
import com.learnwithsubs.feature_video_list.domain.util.OrderType
import com.learnwithsubs.feature_video_list.domain.util.VideoOrder
import kotlinx.coroutines.launch
import javax.inject.Inject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.withContext
import java.util.LinkedList


class VideoListViewModel @Inject constructor(
    val videoListUseCases: VideoListUseCases,
    val videoTranscodeRepository: VideoTranscodeRepository
) : ViewModel() {
    val videoList = MediatorLiveData<List<Video>>()

    val videoProgressLiveData: MutableLiveData<Video?> = videoTranscodeRepository.getVideoProgressLiveData()


    private val videoSemaphore = Semaphore(1)
    private val processQueue = LinkedList<Video?>()


    init {
        updateList()
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

    private fun updateList() {
        videoList.addSource(
            videoListUseCases.getVideoListUseCase.invoke(
                videoOrder = VideoOrder.Date(OrderType.Descending)
            ).asLiveData()
        ) { list ->
            videoList.value = list
        }
    }

    private fun editVideo(video: Video) {
        viewModelScope.launch { videoListUseCases.loadVideoUseCase.invoke(video) }
    }


    /*private fun addVideo(video: Video) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                videoListUseCases.loadVideoUseCase.invoke(video)
            }

            val lastVideo: Video? = withContext(Dispatchers.IO) {
                videoListUseCases.getLastVideoUseCase.invoke()
            }

            //TODO обработать возможный null
            val recodedVideo: Video? = withContext(Dispatchers.IO) {
                lastVideo?.let { videoListUseCases.transcodeVideoUseCase.invoke(it) }
            }

            //TODO обработать возможный null
            withContext(Dispatchers.IO) {
                recodedVideo?.let { videoListUseCases.loadVideoUseCase.invoke(it) }
            }
        }
    }*/


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
                    videoListUseCases.loadVideoUseCase.invoke(poolList)
                    val extractedAudio: Video? = videoListUseCases.extractAudioUseCase.invoke(poolList)

                    // Загрузка аудио
                    poolList.loadingType = VideoLoadingType.GENERATING_SUBTITLES
                    videoListUseCases.loadVideoUseCase.invoke(poolList)
                    videoListUseCases.sendAudioToServerUseCase.invoke(extractedAudio)

                    // Декодирование видео
                    poolList.loadingType = VideoLoadingType.DECODING_VIDEO
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


}