package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.database.domain.models.Video
import com.example.video_transcode.domain.repository.VideoTranscodeRepository

class ExtractVideoPreviewUseCase(private val videoTranscodeRepository: com.example.video_transcode.domain.repository.VideoTranscodeRepository) {
    suspend fun invoke(video: com.learnwithsubs.database.domain.models.Video) {
        videoTranscodeRepository.extractPreview(video = video)
    }
}