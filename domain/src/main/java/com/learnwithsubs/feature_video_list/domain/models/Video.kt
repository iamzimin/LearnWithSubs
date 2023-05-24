package com.learnwithsubs.feature_video_list.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Video(
    @PrimaryKey val id: Int? = null,
    val videoStatus: Int,
    val name: String,
    val preview: Int,
    val duration: Int,
    val watchProgress: Int = 0,
    val saveWords: Int = 0,
    val uploadingProgress: Int = 0,
    val URI: String = " ",
    val timestamp: Long,
    val subtitles: String = "",
) : Parcelable