package com.learnwithsubs.database.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.base.Identifiable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class VideoDBO(
    @PrimaryKey override val id: Int? = null,
    var videoStatus: VideoStatus,
    var loadingType: VideoLoadingType,
    var errorType: VideoErrorType?,
    var isOwnSubtitles: Boolean = false,
    var name: String,
    val duration: Long,
    val bitrate: Int,
    var watchProgress: Int = 0,
    var saveWords: Int = 0,
    var uploadingProgress: Int = 0,
    val URI: String = "",
    val inputPath: String = "",
    var outputPath: String = "",
    val timestamp: Long,
) : Parcelable, Identifiable

enum class VideoStatus(val value: Int) {
    NORMAL_VIDEO(1),
    LOADING_VIDEO(2)
}

enum class VideoLoadingType(val value: Int) {
    WAITING(1),
    EXTRACTING_AUDIO(2),
    DECODING_VIDEO(3),
    LOADING_AUDIO(4),
    GENERATING_SUBTITLES(5),
    DONE(6)
}

enum class VideoErrorType(val value: Int) {
    PROCESSING_VIDEO(1),
    EXTRACTING_AUDIO(2),
    DECODING_VIDEO(3),
    UPLOADING_AUDIO(4),
    GENERATING_SUBTITLES(5),
}