package com.learnwithsubs.feature_video_list.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.learnwithsubs.feature_video_list.data.storage.VideoListDao
import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_view.data.storage.VideoViewDao

@Database(
    entities = [Video::class],
    version = 1
)
abstract class VideoDatabase: RoomDatabase() {
    abstract val videoListDao: VideoListDao
    abstract val videoViewDao: VideoViewDao

    companion object {
        const val DATABASE_NAME = "videos_db"
    }
}