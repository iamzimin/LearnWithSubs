package com.learnwithsubs.feature_video_list.usecase

import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.repository.VideoTranscodeRepository

class ExtractVideoPreviewUseCase(private val videoTranscodeRepository: VideoTranscodeRepository) {
    suspend fun invoke(video: Video) {
        videoTranscodeRepository.extractPreview(video = video)
    }
}