package com.learnwithsubs.database.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.learnwithsubs.database.domain.models.Video

@Database(
    entities = [com.learnwithsubs.database.domain.models.Video::class],
    version = 1
)
abstract class VideoDatabase: RoomDatabase() {
    abstract val videoListDao: VideoListDao

    companion object {
        const val DATABASE_NAME = "videos_db"
    }
}