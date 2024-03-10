package com.learnwithsubs.video_view.domain.models

data class Subtitle(
    val number: Int,
    val startTime: Long,
    val endTime: Long,
    val text: String
)