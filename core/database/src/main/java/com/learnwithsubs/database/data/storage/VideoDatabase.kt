package com.learnwithsubs.database.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [com.learnwithsubs.database.domain.models.VideoDBO::class],
    version = 1
)
abstract class VideoDatabase: RoomDatabase() {
    abstract val videoListDao: VideoListDao

    companion object {
        const val DATABASE_NAME = "videos_db"
    }
}