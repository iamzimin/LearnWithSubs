package com.learnwithsubs.feature_video_view.usecase

import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_view.models.Subtitle
import com.learnwithsubs.feature_video_view.repository.VideoViewRepository

class GetVideoSubtitlesUseCase(
    private val repository: VideoViewRepository
) {
    fun invoke(video: Video?): List<Subtitle> {
        return repository.getVideoSubtitles(video = video)
    }
}