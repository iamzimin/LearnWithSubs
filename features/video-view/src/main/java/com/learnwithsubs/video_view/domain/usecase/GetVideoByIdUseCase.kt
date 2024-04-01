package com.learnwithsubs.video_view.domain.usecase

import com.learnwithsubs.video_view.domain.models.Video
import com.learnwithsubs.video_view.domain.repository.VideoViewRepository
import com.learnwithsubs.video_view.domain.toVideo

class GetVideoByIdUseCase(
    private val repository: VideoViewRepository
) {

    suspend fun invoke(videoID: Int): Video? {
        return repository.getVideoById(videoID = videoID)?.toVideo()
    }
}