package com.learnwithsubs.feature_video_view.domain.models

data class Subtitle(
    val number: Int,
    val startTime: Long,
    val endTime: Long,
    val text: String
)