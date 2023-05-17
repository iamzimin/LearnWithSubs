package com.learnwithsubs.feature_video.domain.usecase

import com.learnwithsubs.feature_video.domain.models.Video
import com.learnwithsubs.feature_video.domain.repository.VideoRepository

class LoadVideoUseCase(
    private val videoRepository: VideoRepository
) {

    suspend operator fun invoke(video: Video) {
        return videoRepository.insertVideo(video)
    }
}