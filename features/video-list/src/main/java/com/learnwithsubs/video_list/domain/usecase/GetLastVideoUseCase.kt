package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.video_list.domain.models.Video
import com.learnwithsubs.video_list.domain.repository.VideoListRepository
import com.learnwithsubs.video_list.domain.toVideo

class GetLastVideoUseCase(
    private val videoListRepository: VideoListRepository
) {
    suspend operator fun invoke(): Video? {
        return videoListRepository.getLastVideo()?.toVideo()
    }
}