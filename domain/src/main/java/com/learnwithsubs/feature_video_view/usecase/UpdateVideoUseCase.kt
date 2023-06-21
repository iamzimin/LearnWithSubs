package com.learnwithsubs.feature_video_view.usecase

import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_view.repository.VideoViewRepository

class UpdateVideoUseCase(
    private val videoViewRepository: VideoViewRepository
) {
    suspend operator fun invoke(video: Video) {
        return videoViewRepository.updateVideo(video)
    }
}