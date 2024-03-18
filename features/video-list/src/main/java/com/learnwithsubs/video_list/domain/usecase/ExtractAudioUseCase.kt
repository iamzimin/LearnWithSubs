package com.learnwithsubs.video_list.domain.usecase

import com.example.video_transcode.domain.models.VideoTranscode
import com.example.video_transcode.domain.repository.VideoTranscodeRepository
import com.learnwithsubs.video_list.domain.models.Video
import com.learnwithsubs.video_list.domain.toVideoTranscode

class ExtractAudioUseCase(
    private val videoTranscodeRepository: VideoTranscodeRepository
) {
    suspend fun invoke(video: Video): VideoTranscode? {
        return videoTranscodeRepository.extractAudio(video.toVideoTranscode())
    }
}