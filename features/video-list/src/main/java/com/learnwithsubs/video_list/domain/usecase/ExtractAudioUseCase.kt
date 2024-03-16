package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.database.domain.models.Video
import com.example.video_transcode.domain.repository.VideoTranscodeRepository

class ExtractAudioUseCase(
    private val videoTranscodeRepository: com.example.video_transcode.domain.repository.VideoTranscodeRepository
) {
    suspend fun invoke(video: models.Video): models.Video? {
        return videoTranscodeRepository.extractAudio(video)
    }
}