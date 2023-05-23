package com.learnwithsubs.feature_video_view.domain.usecase

import com.learnwithsubs.feature_video_view.domain.repository.VideoViewRepository

class GetVideoSubtitlesUseCase(
    private val repository: VideoViewRepository
) {
    fun invoke() {
        repository.getVideoSubtitles()
    }
}