package com.learnwithsubs.feature_video_view.domain.usecase

import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_view.domain.models.Subtitle
import com.learnwithsubs.feature_video_view.domain.repository.VideoViewRepository

class GetVideoSubtitlesUseCase(
    private val repository: VideoViewRepository
) {
    fun invoke(video: Video?): List<Subtitle> {
        return repository.getVideoSubtitles(video = video)
    }
}