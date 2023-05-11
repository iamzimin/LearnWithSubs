package com.learnwithsubs.feature_video.data.storage

data class VideoData(
    val videoType:Int,
    val name:String,
    val preview:Int,
    val duration: Int,
    val saveWords: Int,
    val progress: Int
)
