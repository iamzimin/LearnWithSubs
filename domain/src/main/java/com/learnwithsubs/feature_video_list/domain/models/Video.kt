package com.learnwithsubs.feature_video_list.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Video(
    @PrimaryKey val id: Int? = null,
    var videoStatus: VideoStatus,
    var loadingType: VideoLoadingType,
    var isSelected: Boolean = false,
    var name: String,
    val preview: Int,
    val duration: Int, //TODO mabye Long
    val bitrate: Int,
    var watchProgress: Int = 0,
    var saveWords: Int = 0,
    var uploadingProgress: Int = 0,
    val URI: String = "",
    val inputPath: String = "",
    var outputPath: String = "",
    val timestamp: Long,
) : Parcelable

enum class VideoStatus(val value: Int) {
    NORMAL_VIDEO(1),
    //SELECTED_VIDEO(2),
    LOADING_VIDEO(3)
}

enum class VideoLoadingType(val value: Int) {
    WAITING(1),
    EXTRACTING_AUDIO(2),
    DECODING_VIDEO(3),
    LOADING_AUDIO(4),
    GENERATING_SUBTITLES(5),
    DONE(6)
}