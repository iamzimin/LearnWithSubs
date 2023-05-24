package com.learnwithsubs.feature_video_list.presentation.videos

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.usecase.VideoListUseCases
import com.learnwithsubs.feature_video_list.domain.util.OrderType
import com.learnwithsubs.feature_video_list.domain.util.VideoOrder
import com.learnwithsubs.feature_video_list.presentation.adapter.VideoListAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class VideoListViewModel @Inject constructor(
    val videoListUseCases: VideoListUseCases
) : ViewModel() {

    val videoList = MediatorLiveData<List<Video>>()
    private val currentList = videoList.value?.toMutableList() ?: mutableListOf()
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
        ) {
            videoList.value = it
        }
    }

    private fun editVideo(video: Video) { //TODO temp for test!!!
        viewModelScope.launch {
            videoListUseCases.loadVideoUseCase.invoke(video)
        }
    }

    private fun addVideo(video: Video) {
        currentList.add(video)
        viewModelScope.launch {
            videoListUseCases.loadVideoUseCase.invoke(video)
        }


        //TODO temp for test!!!
        GlobalScope.launch {
            delay(1000)
            val videoListValue: List<Video>? = videoList.value

            if (!videoListValue.isNullOrEmpty()) {
                val lastVideo = videoListValue.first()
                val newVideo = Video(
                    id = lastVideo.id,
                    videoStatus = VideoListAdapter.NORMAL_VIDEO,
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

    /*
    fun updateList() {
        val updatedList: Flow<List<Video>> = videoListUseCases.getVideoListUseCase.invoke(
            videoOrder = VideoOrder.Date(OrderType.Descending)
        )
        videoList.value = runBlocking {
            val videoList = mutableListOf<Video>()
            updatedList.collect { videos ->
                videoList.addAll(videos)
            }
            videoList
        }
    }
     */

//    private fun getVideos(videoOrder: VideoOrder) {
//        videoUseCases.getVideoListUseCase.invoke(videoOrder = videoOrder)
//            .onEach { videos: List<Video> -> }
//    }

}