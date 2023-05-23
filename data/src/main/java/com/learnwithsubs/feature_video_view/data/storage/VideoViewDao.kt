package com.learnwithsubs.feature_video_view.data.storage

import androidx.room.Dao
import androidx.room.Query

@Dao
interface VideoViewDao {

    @Query("SELECT subtitles FROM video")
    fun getSubtitles(): String
}