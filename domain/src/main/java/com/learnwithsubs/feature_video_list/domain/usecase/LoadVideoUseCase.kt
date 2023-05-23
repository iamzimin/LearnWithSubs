package com.learnwithsubs.feature_video_list.domain.usecase

import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.repository.VideoRepository

class LoadVideoUseCase(
    private val videoRepository: VideoRepository
) {

    suspend operator fun invoke(video: Video) {
        return videoRepository.insertVideo(video)
    }
}