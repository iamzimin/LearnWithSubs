package com.learnwithsubs.feature_video_view.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.learnwithsubs.feature_video_list.domain.models.Video

@Dao
interface VideoViewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateVideo(video: Video)
}