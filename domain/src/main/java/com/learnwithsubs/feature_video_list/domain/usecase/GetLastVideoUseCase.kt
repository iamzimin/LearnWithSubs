package com.learnwithsubs.feature_video_list.domain.usecase

import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.repository.VideoListRepository

class GetLastVideoUseCase(
    private val videoListRepository: VideoListRepository
) {

    suspend operator fun invoke(): Video? {
        return videoListRepository.getLastVideo()
    }
}