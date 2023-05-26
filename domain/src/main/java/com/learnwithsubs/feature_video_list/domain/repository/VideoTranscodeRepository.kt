package com.learnwithsubs.feature_video_list.domain.repository

import com.learnwithsubs.feature_video_list.domain.models.Video

interface VideoTranscodeRepository {
    suspend fun transcodeVideo(video: Video): Video?
}