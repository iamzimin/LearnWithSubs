package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.video_list.domain.models.Video
import com.learnwithsubs.video_list.domain.repository.VideoListRepository

class BackOldSubtitlesUseCase(
    private val videoListRepository: VideoListRepository
) {
    suspend fun invoke(video: Video) {
        videoListRepository.backOldSubtitles(video = video)
    }
}