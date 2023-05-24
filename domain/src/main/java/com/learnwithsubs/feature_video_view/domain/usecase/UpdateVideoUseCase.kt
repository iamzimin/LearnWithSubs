package com.learnwithsubs.feature_video_view.domain.usecase

import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_view.domain.repository.VideoViewRepository

class UpdateVideoUseCase(
    private val videoViewRepository: VideoViewRepository
) {
    suspend operator fun invoke(video: Video) {
        return videoViewRepository.updateVideo(video)
    }
}