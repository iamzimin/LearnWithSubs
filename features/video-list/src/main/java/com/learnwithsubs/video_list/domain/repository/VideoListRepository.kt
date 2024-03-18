package com.learnwithsubs.video_list.domain.repository

import com.learnwithsubs.database.domain.models.VideoDBO
import kotlinx.coroutines.flow.Flow

interface VideoListRepository {
    fun getVideos(): Flow<List<VideoDBO>>
    suspend fun getVideoById(id: Int) : VideoDBO?
    suspend fun insertVideo(video: VideoDBO)
    suspend fun deleteVideo(video: VideoDBO)
    suspend fun getLastVideo(): VideoDBO?
    suspend fun saveSubtitles(videoID: Int, subtitles: String)
    suspend fun loadNewSubtitles(videoID: Int, subtitles: String)
    suspend fun backOldSubtitles(videoID: Int)
}