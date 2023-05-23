package com.learnwithsubs.feature_video.data.storage

import androidx.room.*
import com.learnwithsubs.feature_video.domain.models.Video
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

    @Query("SELECT * FROM video")
    fun getVideos(): Flow<List<Video>>

    @Query("SELECT * FROM video WHERE id = :id")
    suspend fun getVideoById(id: Int) : Video?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: Video)

    @Delete
    suspend fun deleteVideo(video: Video)
}