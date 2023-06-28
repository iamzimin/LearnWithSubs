package com.learnwithsubs.feature_video_list.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.usecase.LoadVideoUseCase

interface VideoTranscodeRepository {
    suspend fun transcodeVideo(video: Video): Video?
    suspend fun extractAudio(video: Video): Video?
    fun getVideoProgressLiveData(): MutableLiveData<Video?>
    suspend fun extractPreview(video: Video)
}