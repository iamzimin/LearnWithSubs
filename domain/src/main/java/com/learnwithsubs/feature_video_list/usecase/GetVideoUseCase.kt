package com.learnwithsubs.feature_video_list.usecase

import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.repository.VideoListRepository

class GetVideoUseCase(
    private val videoListRepository: VideoListRepository
) {

    suspend operator fun invoke(id: Int): Video? {
        return videoListRepository.getVideoById(id = id)
    }
}