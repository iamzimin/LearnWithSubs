package com.learnwithsubs.database.data.storage

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoListDao {

    @Query("SELECT * FROM video")
    fun getVideos(): Flow<List<com.learnwithsubs.database.domain.models.VideoDBO>>

    @Query("SELECT * FROM video WHERE id = :id")
    suspend fun getVideoById(id: Int) : com.learnwithsubs.database.domain.models.VideoDBO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(videoDBO: com.learnwithsubs.database.domain.models.VideoDBO)

    @Delete
    suspend fun deleteVideo(videoDBO: com.learnwithsubs.database.domain.models.VideoDBO)

    @Query("SELECT * FROM video ORDER BY id DESC LIMIT 1")
    suspend fun getLastVideo(): com.learnwithsubs.database.domain.models.VideoDBO?
}