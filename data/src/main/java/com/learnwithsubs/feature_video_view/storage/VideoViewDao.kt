package com.learnwithsubs.feature_video_view.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.learnwithsubs.feature_video_list.models.Video

@Dao
interface VideoViewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateVideo(video: Video)
}