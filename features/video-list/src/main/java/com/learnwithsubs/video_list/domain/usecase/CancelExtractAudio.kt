package com.learnwithsubs.video_list.domain.usecase

import com.example.video_transcode.domain.repository.VideoTranscodeRepository

class CancelExtractAudio(
    private val videoTranscodeRepository: VideoTranscodeRepository
) {
    fun invoke() {
        videoTranscodeRepository.cancelExtractAudio()
    }
}