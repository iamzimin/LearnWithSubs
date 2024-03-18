package com.learnwithsubs.video_list.domain.usecase

import com.example.video_transcode.domain.repository.VideoTranscodeRepository

class CancelTranscodeVideo(
    private val videoTranscodeRepository: VideoTranscodeRepository
) {
    fun invoke() {
        videoTranscodeRepository.cancelTranscodeVideo()
    }
}