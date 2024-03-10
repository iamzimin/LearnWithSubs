package com.learnwithsubs.video_list.domain.repository

import androidx.lifecycle.MutableLiveData
import com.learnwithsubs.database.domain.models.Video

interface VideoTranscodeRepository {
    suspend fun transcodeVideo(video: com.learnwithsubs.database.domain.models.Video): com.learnwithsubs.database.domain.models.Video?
    fun cancelTranscodeVideo()
    suspend fun extractAudio(video: com.learnwithsubs.database.domain.models.Video): com.learnwithsubs.database.domain.models.Video?
    fun cancelExtractAudio()
    fun getVideoProgressLiveData(): MutableLiveData<com.learnwithsubs.database.domain.models.Video?>
    suspend fun extractPreview(video: com.learnwithsubs.database.domain.models.Video)
}