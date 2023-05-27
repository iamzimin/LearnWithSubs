package com.learnwithsubs.feature_video_list.presentation.videos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.repository.VideoTranscodeRepository
import com.learnwithsubs.feature_video_list.domain.usecase.VideoListUseCases
import com.learnwithsubs.feature_video_list.domain.util.OrderType
import com.learnwithsubs.feature_video_list.domain.util.VideoOrder
import kotlinx.coroutines.launch
import javax.inject.Inject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class VideoListViewModel @Inject constructor(
    val videoListUseCases: VideoListUseCases,
    videoTranscodeRepository: VideoTranscodeRepository
) : ViewModel() {
    private val videoFrameNumberLiveData: LiveData<Video> = videoTranscodeRepository.getVideoFrameNumberLiveData()


    val videoList = MediatorLiveData<List<Video>>()
    //private var currentList = videoList.value?.toMutableList() ?: mutableListOf()

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

    private fun addVideo(video: Video) {
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
    }

    //fun getVideoFrameNumberLiveData(): LiveData<Int> = videoFrameNumberLiveData
    fun getVideoFrameNumberLiveData(): LiveData<Video> {
        viewModelScope.launch {
            videoFrameNumberLiveData.value?.let { videoListUseCases.loadVideoUseCase.invoke(it) }
        }
        return videoFrameNumberLiveData
    }

    /*
    private fun getVideos(videoOrder: VideoOrder) {
        videoUseCases.getVideoListUseCase.invoke(videoOrder = videoOrder)
            .onEach { videos: List<Video> -> }
    }*/
}