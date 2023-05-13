package com.learnwithsubs.feature_video.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.learnwithsubs.feature_video.domain.models.Video

@Database(
    entities = [Video::class],
    version = 1
)
abstract class VideoDatabase: RoomDatabase() {
    abstract val videoDao: VideoDao

    companion object {
        const val DATABASE_NAME = "videos_db"
    }
}