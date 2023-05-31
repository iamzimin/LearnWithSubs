package com.learnwithsubs.feature_video_list.domain.usecase

import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.repository.VideoTranscodeRepository

class ExtractAudioUseCase(
    private val videoTranscodeRepository: VideoTranscodeRepository
) {
    suspend fun invoke(video: Video): String? {
        return videoTranscodeRepository.extractAudio(video)
    }
}