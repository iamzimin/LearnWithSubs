package com.learnwithsubs.feature_video_list.usecase

import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.repository.VideoListRepository

class GetLastVideoUseCase(
    private val videoListRepository: VideoListRepository
) {

    suspend operator fun invoke(): Video? {
        return videoListRepository.getLastVideo()
    }
}