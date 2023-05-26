package com.learnwithsubs.feature_video_list.domain.repository

import com.learnwithsubs.feature_video_list.domain.models.Video
import kotlinx.coroutines.flow.Flow

interface VideoListRepository {

    fun getVideos(): Flow<List<Video>>

    suspend fun getVideoById(id: Int) : Video?

    suspend fun insertVideo(video: Video)

    suspend fun deleteVideo(video: Video)

    suspend fun getLastVideo(): Video?

}