package com.learnwithsubs.feature_video_list.usecase

import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.repository.VideoListRepository

class LoadVideoUseCase(
    private val videoListRepository: VideoListRepository
) {

    suspend operator fun invoke(video: Video) {
        return videoListRepository.insertVideo(video)
    }
}