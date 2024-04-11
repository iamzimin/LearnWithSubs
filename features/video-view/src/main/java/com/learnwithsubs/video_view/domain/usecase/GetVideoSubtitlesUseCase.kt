package com.learnwithsubs.video_view.domain.usecase

import com.learnwithsubs.video_view.domain.models.Subtitle
import com.learnwithsubs.video_view.domain.models.Video
import com.learnwithsubs.video_view.domain.repository.VideoViewRepository

class GetVideoSubtitlesUseCase(
    private val repository: VideoViewRepository
) {
    fun invoke(video: Video): List<Subtitle>? {
        return repository.getVideoSubtitles(video = video)
    }
}