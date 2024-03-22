package com.learnwithsubs.video_view.domain.usecase

import com.learnwithsubs.video_view.domain.models.Video
import com.learnwithsubs.video_view.domain.repository.VideoViewRepository
import com.learnwithsubs.video_view.domain.toVideoDBO

class UpdateVideoUseCase(
    private val videoViewRepository: VideoViewRepository
) {
    suspend operator fun invoke(video: Video) {
        return videoViewRepository.updateVideo(video = video.toVideoDBO())
    }
}