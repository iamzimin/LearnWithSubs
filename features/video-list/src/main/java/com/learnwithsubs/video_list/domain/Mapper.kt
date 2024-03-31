package com.learnwithsubs.video_list.domain

import com.example.video_transcode.domain.models.VideoTranscode
import com.example.video_transcode.domain.models.VideoTranscodeErrorType
import com.example.video_transcode.domain.models.VideoTranscodeLoadingType
import com.example.video_transcode.domain.models.VideoTranscodeStatus
import com.learnwithsubs.database.domain.models.VideoDBO
import com.learnwithsubs.database.domain.models.VideoErrorTypeDBO
import com.learnwithsubs.database.domain.models.VideoLoadingTypeDBO
import com.learnwithsubs.database.domain.models.VideoStatusDBO
import com.learnwithsubs.video_list.domain.models.Video
import com.learnwithsubs.video_list.domain.models.VideoErrorType
import com.learnwithsubs.video_list.domain.models.VideoLoadingType
import com.learnwithsubs.video_list.domain.models.VideoStatus

internal fun Video.toVideoDBO() : VideoDBO {
    return VideoDBO(
        id = this.id,
        videoStatus = this.videoStatus.toVideoStatusDBO(),
        loadingType = this.loadingType.toVideoLoadingTypeDBO(),
        errorType = this.errorType?.toVideoErrorTypeDBO(),
        isOwnSubtitles = this.isOwnSubtitles,
        name = this.name,
        duration = this.duration,
        bitrate = this.bitrate,
        watchProgress = this.watchProgress,
        saveWords = this.saveWords,
        uploadingProgress = this.uploadingProgress,
        URI = this.URI,
        inputPath = this.inputPath,
        outputPath = this.outputPath,
        timestamp = this.timestamp
    )
}

internal fun VideoDBO.toVideo() : Video {
    return Video(
        id = this.id,
        videoStatus = this.videoStatus.toVideoStatus(),
        loadingType = this.loadingType.toVideoLoadingType(),
        errorType = this.errorType?.toVideoErrorType(),
        isOwnSubtitles = this.isOwnSubtitles,
        name = this.name,
        duration = this.duration,
        bitrate = this.bitrate,
        watchProgress = this.watchProgress,
        saveWords = this.saveWords,
        uploadingProgress = this.uploadingProgress,
        URI = this.URI,
        inputPath = this.inputPath,
        outputPath = this.outputPath,
        timestamp = this.timestamp
    )
}

internal fun VideoTranscode.toVideo() : Video {
    return Video(
        id = this.id,
        videoStatus = this.videoStatus.toVideoStatus(),
        loadingType = this.loadingType.toVideoLoadingType(),
        errorType = this.errorType?.toVideoErrorType(),
        isOwnSubtitles = this.isOwnSubtitles,
        name = this.name,
        duration = this.duration,
        bitrate = this.bitrate,
        watchProgress = this.watchProgress,
        saveWords = this.saveWords,
        uploadingProgress = this.uploadingProgress,
        URI = this.URI,
        inputPath = this.inputPath,
        outputPath = this.outputPath,
        timestamp = this.timestamp
    )
}

internal fun Video.toVideoTranscode() : VideoTranscode {
    return VideoTranscode(
        id = this.id,
        videoStatus = this.videoStatus.toVideoTranscodeStatus(),
        loadingType = this.loadingType.toVideoTranscodeLoadingType(),
        errorType = this.errorType?.toVideoTranscodeErrorType(),
        isOwnSubtitles = this.isOwnSubtitles,
        name = this.name,
        duration = this.duration,
        bitrate = this.bitrate,
        watchProgress = this.watchProgress,
        saveWords = this.saveWords,
        uploadingProgress = this.uploadingProgress,
        URI = this.URI,
        inputPath = this.inputPath,
        outputPath = this.outputPath,
        timestamp = this.timestamp
    )
}




internal fun VideoStatus.toVideoStatusDBO(): VideoStatusDBO {
    return when (this) {
        VideoStatus.NORMAL_VIDEO -> VideoStatusDBO.NORMAL_VIDEO
        VideoStatus.LOADING_VIDEO -> VideoStatusDBO.LOADING_VIDEO
    }
}
internal fun VideoLoadingType.toVideoLoadingTypeDBO(): VideoLoadingTypeDBO {
    return when (this) {
        VideoLoadingType.WAITING -> VideoLoadingTypeDBO.WAITING
        VideoLoadingType.EXTRACTING_AUDIO -> VideoLoadingTypeDBO.EXTRACTING_AUDIO
        VideoLoadingType.DECODING_VIDEO -> VideoLoadingTypeDBO.DECODING_VIDEO
        VideoLoadingType.LOADING_AUDIO -> VideoLoadingTypeDBO.LOADING_AUDIO
        VideoLoadingType.GENERATING_SUBTITLES -> VideoLoadingTypeDBO.GENERATING_SUBTITLES
        VideoLoadingType.DONE -> VideoLoadingTypeDBO.DONE
    }
}
internal fun VideoErrorType.toVideoErrorTypeDBO(): VideoErrorTypeDBO {
    return when (this) {
        VideoErrorType.PROCESSING_VIDEO -> VideoErrorTypeDBO.PROCESSING_VIDEO
        VideoErrorType.EXTRACTING_AUDIO -> VideoErrorTypeDBO.EXTRACTING_AUDIO
        VideoErrorType.DECODING_VIDEO -> VideoErrorTypeDBO.DECODING_VIDEO
        VideoErrorType.UPLOADING_AUDIO -> VideoErrorTypeDBO.UPLOADING_AUDIO
        VideoErrorType.GENERATING_SUBTITLES -> VideoErrorTypeDBO.GENERATING_SUBTITLES
    }
}




internal fun VideoStatusDBO.toVideoStatus(): VideoStatus {
    return when (this) {
        VideoStatusDBO.NORMAL_VIDEO -> VideoStatus.NORMAL_VIDEO
        VideoStatusDBO.LOADING_VIDEO -> VideoStatus.LOADING_VIDEO
    }
}
internal fun VideoLoadingTypeDBO.toVideoLoadingType(): VideoLoadingType {
    return when (this) {
        VideoLoadingTypeDBO.WAITING -> VideoLoadingType.WAITING
        VideoLoadingTypeDBO.EXTRACTING_AUDIO -> VideoLoadingType.EXTRACTING_AUDIO
        VideoLoadingTypeDBO.DECODING_VIDEO -> VideoLoadingType.DECODING_VIDEO
        VideoLoadingTypeDBO.LOADING_AUDIO -> VideoLoadingType.LOADING_AUDIO
        VideoLoadingTypeDBO.GENERATING_SUBTITLES -> VideoLoadingType.GENERATING_SUBTITLES
        VideoLoadingTypeDBO.DONE -> VideoLoadingType.DONE
    }
}
internal fun VideoErrorTypeDBO.toVideoErrorType(): VideoErrorType {
    return when (this) {
        VideoErrorTypeDBO.PROCESSING_VIDEO -> VideoErrorType.PROCESSING_VIDEO
        VideoErrorTypeDBO.EXTRACTING_AUDIO -> VideoErrorType.EXTRACTING_AUDIO
        VideoErrorTypeDBO.DECODING_VIDEO -> VideoErrorType.DECODING_VIDEO
        VideoErrorTypeDBO.UPLOADING_AUDIO -> VideoErrorType.UPLOADING_AUDIO
        VideoErrorTypeDBO.GENERATING_SUBTITLES -> VideoErrorType.GENERATING_SUBTITLES
    }
}








internal fun VideoStatus.toVideoTranscodeStatus(): VideoTranscodeStatus {
    return when (this) {
        VideoStatus.NORMAL_VIDEO -> VideoTranscodeStatus.NORMAL_VIDEO
        VideoStatus.LOADING_VIDEO -> VideoTranscodeStatus.LOADING_VIDEO
    }
}
internal fun VideoLoadingType.toVideoTranscodeLoadingType(): VideoTranscodeLoadingType {
    return when (this) {
        VideoLoadingType.WAITING -> VideoTranscodeLoadingType.WAITING
        VideoLoadingType.EXTRACTING_AUDIO -> VideoTranscodeLoadingType.EXTRACTING_AUDIO
        VideoLoadingType.DECODING_VIDEO -> VideoTranscodeLoadingType.DECODING_VIDEO
        VideoLoadingType.LOADING_AUDIO -> VideoTranscodeLoadingType.LOADING_AUDIO
        VideoLoadingType.GENERATING_SUBTITLES -> VideoTranscodeLoadingType.GENERATING_SUBTITLES
        VideoLoadingType.DONE -> VideoTranscodeLoadingType.DONE
    }
}
internal fun VideoErrorType.toVideoTranscodeErrorType(): VideoTranscodeErrorType {
    return when (this) {
        VideoErrorType.PROCESSING_VIDEO -> VideoTranscodeErrorType.PROCESSING_VIDEO
        VideoErrorType.EXTRACTING_AUDIO -> VideoTranscodeErrorType.EXTRACTING_AUDIO
        VideoErrorType.DECODING_VIDEO -> VideoTranscodeErrorType.DECODING_VIDEO
        VideoErrorType.UPLOADING_AUDIO -> VideoTranscodeErrorType.UPLOADING_AUDIO
        VideoErrorType.GENERATING_SUBTITLES -> VideoTranscodeErrorType.GENERATING_SUBTITLES
    }
}




internal fun VideoTranscodeStatus.toVideoStatus(): VideoStatus {
    return when (this) {
        VideoTranscodeStatus.NORMAL_VIDEO -> VideoStatus.NORMAL_VIDEO
        VideoTranscodeStatus.LOADING_VIDEO -> VideoStatus.LOADING_VIDEO
    }
}
internal fun VideoTranscodeLoadingType.toVideoLoadingType(): VideoLoadingType {
    return when (this) {
        VideoTranscodeLoadingType.WAITING -> VideoLoadingType.WAITING
        VideoTranscodeLoadingType.EXTRACTING_AUDIO -> VideoLoadingType.EXTRACTING_AUDIO
        VideoTranscodeLoadingType.DECODING_VIDEO -> VideoLoadingType.DECODING_VIDEO
        VideoTranscodeLoadingType.LOADING_AUDIO -> VideoLoadingType.LOADING_AUDIO
        VideoTranscodeLoadingType.GENERATING_SUBTITLES -> VideoLoadingType.GENERATING_SUBTITLES
        VideoTranscodeLoadingType.DONE -> VideoLoadingType.DONE
    }
}
internal fun VideoTranscodeErrorType.toVideoErrorType(): VideoErrorType {
    return when (this) {
        VideoTranscodeErrorType.PROCESSING_VIDEO -> VideoErrorType.PROCESSING_VIDEO
        VideoTranscodeErrorType.EXTRACTING_AUDIO -> VideoErrorType.EXTRACTING_AUDIO
        VideoTranscodeErrorType.DECODING_VIDEO -> VideoErrorType.DECODING_VIDEO
        VideoTranscodeErrorType.UPLOADING_AUDIO -> VideoErrorType.UPLOADING_AUDIO
        VideoTranscodeErrorType.GENERATING_SUBTITLES -> VideoErrorType.GENERATING_SUBTITLES
    }
}
