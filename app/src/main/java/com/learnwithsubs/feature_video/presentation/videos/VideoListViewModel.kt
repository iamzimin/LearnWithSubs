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
import com.learnwithsubs.feature_video.presentation.adapter.VideoAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    fun onEvent(event: VideosEvent) {
        when (event) {
            is VideosEvent.Order -> {
                //if (false) TODO add a check for the same choice
                //return getVideos(videoOrder = event.videoOrder)
            }
            is VideosEvent.DeleteVideo -> {
                viewModelScope.launch {
                    videoUseCases.deleteVideoUseCase.invoke(event.video)
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

    private fun editVideo(video: Video) { //TODO temp for test!!!
        viewModelScope.launch {
            videoUseCases.loadVideoUseCase.invoke(video)
        }
    }

    private fun addVideo(video: Video) {
        currentList.add(video)
        viewModelScope.launch {
            videoUseCases.loadVideoUseCase.invoke(video)
        }


        //TODO temp for test!!!
        GlobalScope.launch {
            delay(2000)
            val videoListValue: List<Video>? = videoList.value

            if (!videoListValue.isNullOrEmpty()) {
                val lastVideo = videoListValue.first()
                val newVideo = Video(
                    id = lastVideo.id,
                    videoStatus = VideoAdapter.NORMAL_VIDEO,
                    name = lastVideo.name,
                    preview = 0,
                    duration = lastVideo.duration,
                    URI = lastVideo.URI,
                    timestamp = lastVideo.timestamp
                )
                editVideo(video = newVideo)
            }
        }



        videoList.value = currentList.toList()
    }

//    private fun getVideos(videoOrder: VideoOrder) {
//        videoUseCases.getVideoListUseCase.invoke(videoOrder = videoOrder)
//            .onEach { videos: List<Video> -> }
//    }

}