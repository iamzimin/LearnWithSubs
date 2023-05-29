package com.learnwithsubs.feature_video_list.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.learnwithsubs.feature_video_list.domain.models.Video

interface VideoTranscodeRepository {
    suspend fun transcodeVideo(video: Video): Video?
    fun getVideoProgressLiveData(): MutableLiveData<List<Video>?>
}