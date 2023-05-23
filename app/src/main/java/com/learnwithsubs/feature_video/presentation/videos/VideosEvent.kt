package com.learnwithsubs.feature_video.presentation.videos

import com.learnwithsubs.feature_video.domain.models.Video
import com.learnwithsubs.feature_video.domain.util.VideoOrder

sealed class VideosEvent {
    data class Order(val videoOrder: VideoOrder): VideosEvent()
    data class DeleteVideo(val video: Video): VideosEvent()
    data class LoadVideo(val video: Video): VideosEvent()
    data class UpdateVideo(val video: Video): VideosEvent()
//    object ToggleOrderSection: VideosEvent()
}
