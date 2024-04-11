package com.example.video_transcode.domain.models

data class VideoTranscode(
    val id: Int? = null,
    var errorType: VideoTranscodeErrorType?,
    val duration: Long,
    var uploadingProgress: Int = 0,
    val inputPath: String = "",
    var outputPath: String = "",
)

enum class VideoTranscodeErrorType(val value: Int) {
    EXTRACTING_AUDIO(2),
    DECODING_VIDEO(3),
}
