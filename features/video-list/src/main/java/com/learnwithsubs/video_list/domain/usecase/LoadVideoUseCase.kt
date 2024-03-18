package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.video_list.domain.models.Video
import com.learnwithsubs.video_list.domain.repository.VideoListRepository
import com.learnwithsubs.video_list.domain.toVideoDBO

class LoadVideoUseCase(
    private val videoListRepository: VideoListRepository
) {

    suspend operator fun invoke(video: Video) {
        return videoListRepository.insertVideo(video.toVideoDBO())
    }
}