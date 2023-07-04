package com.learnwithsubs.feature_video_list.repository

import com.learnwithsubs.feature_video_list.models.Video
import kotlinx.coroutines.flow.Flow

interface VideoListRepository {
    fun getVideos(): Flow<List<Video>>
    suspend fun getVideoById(id: Int) : Video?
    suspend fun insertVideo(video: Video)
    suspend fun deleteVideo(video: Video)
    suspend fun getLastVideo(): Video?
    suspend fun loadNewSubtitles(video: Video, subtitles: String)
    suspend fun backOldSubtitles(video: Video)
}