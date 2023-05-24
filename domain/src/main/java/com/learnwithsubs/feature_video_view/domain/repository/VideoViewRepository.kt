package com.learnwithsubs.feature_video_view.domain.repository

import com.learnwithsubs.feature_video_list.domain.models.Video

interface VideoViewRepository {
    fun getVideoSubtitles(): String

    suspend fun updateVideo(video: Video)
}