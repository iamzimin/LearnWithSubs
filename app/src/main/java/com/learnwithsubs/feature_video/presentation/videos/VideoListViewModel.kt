package com.learnwithsubs.feature_video.presentation.videos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.learnwithsubs.feature_video.domain.models.Video
import com.learnwithsubs.feature_video.domain.usecase.VideoUseCases
import com.learnwithsubs.feature_video.domain.util.OrderType
import com.learnwithsubs.feature_video.domain.util.VideoOrder
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class VideoListViewModel @Inject constructor(
    val videoUseCases: VideoUseCases
) : ViewModel() {

    val videoList = MediatorLiveData<List<Video>>()
    private val currentList = videoList.value?.toMutableList() ?: mutableListOf()
    init {
        videoList.addSource(
            videoUseCases.getVideoListUseCase.invoke(
                videoOrder = VideoOrder.Date(OrderType.Descending)
            ).asLiveData()
        ) {
            videoList.value = it
        }
    }

    fun addVideo(video: Video) {
        currentList.add(video)
        videoList.value = currentList.toList()
    }

    fun onEvent(event: VideosEvent) {
        when (event) {
            is VideosEvent.Order -> {
                //if (false) TODO add a check for the same choice
                //return getVideos(videoOrder = event.videoOrder)
            }
            is VideosEvent.DeleteVideo -> {
                viewModelScope.launch {
                    videoUseCases.deleteVideoUseCase(event.video)
                }
            }
        }
    }

//    private fun getVideos(videoOrder: VideoOrder) {
//        videoUseCases.getVideoListUseCase.invoke(videoOrder = videoOrder)
//            .onEach { videos: List<Video> -> }
//    }

}