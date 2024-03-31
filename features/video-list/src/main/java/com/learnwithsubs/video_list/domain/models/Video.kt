package com.learnwithsubs.video_list.domain.models

import android.os.Parcel
import android.os.Parcelable
import com.example.base.Identifiable

data class Video(
    override val id: Int? = null,
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
) : Parcelable, Identifiable  {

    constructor(parcel: Parcel) : this(
        id = parcel.readValue(Int::class.java.classLoader) as? Int,
        videoStatus = parcel.readSerializable() as VideoStatus,
        loadingType = parcel.readSerializable() as VideoLoadingType,
        errorType = parcel.readSerializable() as? VideoErrorType,
        isOwnSubtitles = parcel.readByte() != 0.toByte(),
        name = parcel.readString()!!,
        duration = parcel.readLong(),
        bitrate = parcel.readInt(),
        watchProgress = parcel.readInt(),
        saveWords = parcel.readInt(),
        uploadingProgress = parcel.readInt(),
        URI = parcel.readString()!!,
        inputPath = parcel.readString()!!,
        outputPath = parcel.readString()!!,
        timestamp = parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        with(parcel) {
            writeValue(id)
            writeSerializable(videoStatus)
            writeSerializable(loadingType)
            writeSerializable(errorType)
            writeByte(if (isOwnSubtitles) 1 else 0)
            writeString(name)
            writeLong(duration)
            writeInt(bitrate)
            writeInt(watchProgress)
            writeInt(saveWords)
            writeInt(uploadingProgress)
            writeString(URI)
            writeString(inputPath)
            writeString(outputPath)
            writeLong(timestamp)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Video> {
        override fun createFromParcel(parcel: Parcel): Video {
            return Video(parcel)
        }

        override fun newArray(size: Int): Array<Video?> {
            return arrayOfNulls(size)
        }
    }
}

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