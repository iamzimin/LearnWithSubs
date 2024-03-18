package com.example.video_transcode.domain.repository

import androidx.lifecycle.MutableLiveData
import com.example.video_transcode.domain.models.VideoTranscode

interface VideoTranscodeRepository {
    suspend fun transcodeVideo(videoTranscode: VideoTranscode): VideoTranscode?
    fun cancelTranscodeVideo()
    suspend fun extractAudio(videoTranscode: VideoTranscode): VideoTranscode?
    fun cancelExtractAudio()
    fun getVideoProgressLiveData(): MutableLiveData<VideoTranscode?>
    suspend fun extractPreview(videoTranscode: VideoTranscode)
}