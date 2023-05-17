package com.learnwithsubs.feature_video.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Video(
    @PrimaryKey val id: Int? = null,
    val videoStatus:Int,
    val name:String,
    val preview:Int,
    val duration: Long,
    val saveWords: Int = 0,
    val progress: Int = 0,
    //val uploadingProgress: Int = 0,
    val URI: String,
    val timestamp: Long
)
