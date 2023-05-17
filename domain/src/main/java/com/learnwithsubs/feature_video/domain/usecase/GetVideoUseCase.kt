package com.learnwithsubs.feature_video.domain.usecase

import com.learnwithsubs.feature_video.domain.models.Video
import com.learnwithsubs.feature_video.domain.repository.VideoRepository

class GetVideoUseCase(
    private val videoRepository: VideoRepository
) {

    suspend operator fun invoke(id: Int): Video? {
        return videoRepository.getVideoById(id = id)
    }
}