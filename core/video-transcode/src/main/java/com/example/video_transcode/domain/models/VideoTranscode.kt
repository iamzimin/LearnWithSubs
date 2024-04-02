package com.example.video_transcode.domain.models

data class VideoTranscode(
    val id: Int? = null,
    var videoStatus: VideoTranscodeStatus,
    var loadingType: VideoTranscodeLoadingType,
    var errorType: VideoTranscodeErrorType?,
    var isOwnSubtitles: Boolean = false,
    var name: String,
    val duration: Long,
    var watchProgress: Int = 0,
    var saveWords: Int = 0,
    var uploadingProgress: Int = 0,
    val URI: String = "",
    val inputPath: String = "",
    var outputPath: String = "",
    val timestamp: Long,
)

enum class VideoTranscodeStatus(val value: Int) {
    NORMAL_VIDEO(1),
    LOADING_VIDEO(2)
}

enum class VideoTranscodeLoadingType(val value: Int) {
    WAITING(1),
    EXTRACTING_AUDIO(2),
    DECODING_VIDEO(3),
    LOADING_AUDIO(4),
    GENERATING_SUBTITLES(5),
    DONE(6)
}

enum class VideoTranscodeErrorType(val value: Int) {
    PROCESSING_VIDEO(1),
    EXTRACTING_AUDIO(2),
    DECODING_VIDEO(3),
    UPLOADING_AUDIO(4),
    GENERATING_SUBTITLES(5),
}
