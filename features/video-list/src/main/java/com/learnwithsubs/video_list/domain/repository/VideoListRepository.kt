package com.learnwithsubs.video_list.domain.repository

import com.learnwithsubs.database.domain.models.Video
import kotlinx.coroutines.flow.Flow

interface VideoListRepository {
    fun getVideos(): Flow<List<com.learnwithsubs.database.domain.models.Video>>
    suspend fun getVideoById(id: Int) : com.learnwithsubs.database.domain.models.Video?
    suspend fun insertVideo(video: com.learnwithsubs.database.domain.models.Video)
    suspend fun deleteVideo(video: com.learnwithsubs.database.domain.models.Video)
    suspend fun getLastVideo(): com.learnwithsubs.database.domain.models.Video?
    suspend fun saveSubtitles(video: com.learnwithsubs.database.domain.models.Video, subtitles: String)
    suspend fun loadNewSubtitles(video: com.learnwithsubs.database.domain.models.Video, subtitles: String)
    suspend fun backOldSubtitles(video: com.learnwithsubs.database.domain.models.Video)
}