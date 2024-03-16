package com.example.video_transcode.domain.repository

import androidx.lifecycle.MutableLiveData
import Video

interface VideoTranscodeRepository {
    suspend fun transcodeVideo(video: Video): Video?
    fun cancelTranscodeVideo()
    suspend fun extractAudio(video: Video): Video?
    fun cancelExtractAudio()
    fun getVideoProgressLiveData(): MutableLiveData<Video?>
    suspend fun extractPreview(video: Video)
}