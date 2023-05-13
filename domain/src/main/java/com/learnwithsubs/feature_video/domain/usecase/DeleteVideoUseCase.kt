package com.learnwithsubs.feature_video.domain.usecase

import com.learnwithsubs.feature_video.domain.models.Video
import com.learnwithsubs.feature_video.domain.repository.VideoRepository

class DeleteVideoUseCase(
    private val repository: VideoRepository
) {
    suspend operator fun invoke(video: Video) {
        repository.deleteVideo(video)
    }
}