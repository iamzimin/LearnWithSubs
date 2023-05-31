package com.learnwithsubs.feature_video_list.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.usecase.LoadVideoUseCase

interface VideoTranscodeRepository {
    suspend fun transcodeVideo(video: Video): Video?
    suspend fun extractAudio(video: Video): String?
    fun getVideoProgressLiveData(): MutableLiveData<Video?>
}