package com.learnwithsubs.feature_video_list.domain.usecase

import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.repository.VideoListRepository

class GetVideoUseCase(
    private val videoListRepository: VideoListRepository
) {

    suspend operator fun invoke(id: Int): Video? {
        return videoListRepository.getVideoById(id = id)
    }
}