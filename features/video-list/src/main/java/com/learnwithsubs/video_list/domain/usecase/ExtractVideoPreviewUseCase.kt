package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.database.domain.models.Video
import com.example.video_transcode.domain.repository.VideoTranscodeRepository

class ExtractVideoPreviewUseCase(private val videoTranscodeRepository: VideoTranscodeRepository) {
    suspend fun invoke(video: models.Video) {
        videoTranscodeRepository.extractPreview(video = video)
    }
}