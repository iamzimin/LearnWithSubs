package com.learnwithsubs.feature_video_list.storage

import androidx.room.*
import com.learnwithsubs.feature_video_list.models.Video
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoListDao {

    @Query("SELECT * FROM video")
    fun getVideos(): Flow<List<Video>>

    @Query("SELECT * FROM video WHERE id = :id")
    suspend fun getVideoById(id: Int) : Video?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: Video)

    @Delete
    suspend fun deleteVideo(video: Video)

    @Query("SELECT * FROM video ORDER BY id DESC LIMIT 1")
    suspend fun getLastVideo(): Video?
}