package com.learnwithsubs.feature_video.domain.repository

import com.learnwithsubs.feature_video.domain.models.Video
import kotlinx.coroutines.flow.Flow

interface VideoRepository {

    fun getVideos(): Flow<List<Video>>

    suspend fun getNoteById(id: Int) : Video?

    suspend fun insertVideo(video: Video)

    suspend fun deleteVideo(video: Video)

}