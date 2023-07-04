package com.learnwithsubs.feature_video_list.usecase

import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.repository.VideoListRepository

class BackOldSubtitlesUseCase(
    private val videoListRepository: VideoListRepository
) {
    suspend fun invoke(video: Video) {
        videoListRepository.backOldSubtitles(video = video)
    }
}