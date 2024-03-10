package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.video_list.domain.models.Video
import com.learnwithsubs.video_list.domain.repository.VideoTranscodeRepository

class ExtractVideoPreviewUseCase(private val videoTranscodeRepository: VideoTranscodeRepository) {
    suspend fun invoke(video: Video) {
        videoTranscodeRepository.extractPreview(video = video)
    }
}