package com.learnwithsubs.feature_video.presentation.videos

import com.learnwithsubs.feature_video.domain.models.Video
import com.learnwithsubs.feature_video.domain.util.OrderType
import com.learnwithsubs.feature_video.domain.util.VideoOrder

data class VideosState(
    val videos: List<Video> = emptyList(),
    val noteOrder: VideoOrder = VideoOrder.Date(OrderType.Descending),
    val isOrderSelectionVisible: Boolean = false //TODO
)
