package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.video_list.domain.repository.VideoListRepository

class BackOldSubtitlesUseCase(
    private val videoListRepository: VideoListRepository
) {
    suspend fun invoke(videoID: Int) {
        videoListRepository.backOldSubtitles(videoID = videoID)
    }
}