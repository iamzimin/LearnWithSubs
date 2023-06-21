package com.learnwithsubs.feature_video_list.videos

import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.util.OrderType
import com.learnwithsubs.feature_video_list.util.VideoOrder

data class VideosState(
    val videos: List<Video> = emptyList(),
    val noteOrder: VideoOrder = VideoOrder.Date(OrderType.Descending),
    val isOrderSelectionVisible: Boolean = false //TODO
)
