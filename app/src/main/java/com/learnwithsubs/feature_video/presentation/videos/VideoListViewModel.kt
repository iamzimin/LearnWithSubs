package com.learnwithsubs.feature_video.presentation.videos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnwithsubs.databinding.VideoListBinding
import com.learnwithsubs.feature_video.domain.models.Video
import com.learnwithsubs.feature_video.domain.usecase.VideoUseCases
import com.learnwithsubs.feature_video.domain.util.VideoOrder
import com.learnwithsubs.feature_video.presentation.adapter.VideoAdapter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
class VideoListViewModel @Inject constructor(
    val videoUseCases: VideoUseCases,

) : ViewModel() {

//    lateinit public var binding: VideoListBinding
//    private val adapter = VideoAdapter()

//    init {
//        binding = VideoListBinding.inflate(layoutInflater)
//    }

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