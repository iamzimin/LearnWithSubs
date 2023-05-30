package com.learnwithsubs.feature_video_view.domain.repository

import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_view.domain.models.Subtitle

interface VideoViewRepository {
    fun getVideoSubtitles(video: Video?): List<Subtitle>

    suspend fun updateVideo(video: Video)
}