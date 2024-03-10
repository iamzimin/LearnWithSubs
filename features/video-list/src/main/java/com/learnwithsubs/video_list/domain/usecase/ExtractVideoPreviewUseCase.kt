package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.database.domain.models.Video
import com.learnwithsubs.video_list.domain.repository.VideoTranscodeRepository

class ExtractVideoPreviewUseCase(private val videoTranscodeRepository: VideoTranscodeRepository) {
    suspend fun invoke(video: com.learnwithsubs.database.domain.models.Video) {
        videoTranscodeRepository.extractPreview(video = video)
    }
}