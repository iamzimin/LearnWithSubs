package com.learnwithsubs.database.data.storage

import androidx.room.*
import com.learnwithsubs.database.domain.models.Video
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoListDao {

    @Query("SELECT * FROM video")
    fun getVideos(): Flow<List<com.learnwithsubs.database.domain.models.Video>>

    @Query("SELECT * FROM video WHERE id = :id")
    suspend fun getVideoById(id: Int) : com.learnwithsubs.database.domain.models.Video?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: com.learnwithsubs.database.domain.models.Video)

    @Delete
    suspend fun deleteVideo(video: com.learnwithsubs.database.domain.models.Video)

    @Query("SELECT * FROM video ORDER BY id DESC LIMIT 1")
    suspend fun getLastVideo(): com.learnwithsubs.database.domain.models.Video?
}