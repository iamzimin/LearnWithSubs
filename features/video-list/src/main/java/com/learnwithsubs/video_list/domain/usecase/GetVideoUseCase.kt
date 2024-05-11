package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.video_list.domain.models.Video
import com.learnwithsubs.video_list.domain.repository.VideoListRepository
import com.learnwithsubs.video_list.domain.toVideo

class GetVideoUseCase(
    private val videoListRepository: VideoListRepository
) {
    suspend operator fun invoke(id: Int): Video? {
        return videoListRepository.getVideoById(id = id)?.toVideo()
    }
}