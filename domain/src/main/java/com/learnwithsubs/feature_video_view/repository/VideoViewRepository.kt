package com.learnwithsubs.feature_video_view.repository

import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_view.models.Subtitle

interface VideoViewRepository {
    fun getVideoSubtitles(video: Video?): List<Subtitle>

    suspend fun updateVideo(video: Video)
}