package com.learnwithsubs.database.data.storage

import androidx.room.*
import com.learnwithsubs.database.domain.models.VideoDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoListDao {

    @Query("SELECT * FROM videodbo")
    fun getVideos(): Flow<List<VideoDBO>>

    @Query("SELECT * FROM videodbo WHERE id = :id")
    suspend fun getVideoById(id: Int) : VideoDBO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(videoDBO: VideoDBO)

    @Query("UPDATE videodbo SET name = :name, watchProgress = :watchProgress, saveWords = :saveWords, outputPath = :outputPath WHERE id = :id")
    suspend fun updateVideo(id: Int?, name: String, watchProgress: Int?, saveWords: Int?, outputPath: String?)

    @Delete
    suspend fun deleteVideo(videoDBO: VideoDBO)

    @Query("SELECT * FROM videodbo ORDER BY id DESC LIMIT 1")
    suspend fun getLastVideo(): VideoDBO?
}