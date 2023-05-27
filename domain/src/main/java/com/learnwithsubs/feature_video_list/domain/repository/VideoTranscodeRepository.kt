package com.learnwithsubs.feature_video_list.domain.repository

import androidx.lifecycle.LiveData
import com.learnwithsubs.feature_video_list.domain.models.Video

interface VideoTranscodeRepository {
    suspend fun transcodeVideo(video: Video): Video?
    fun getVideoFrameNumberLiveData(): LiveData<Video>
}