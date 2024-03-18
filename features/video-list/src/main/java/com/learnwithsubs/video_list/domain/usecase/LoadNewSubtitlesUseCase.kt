package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.video_list.domain.repository.VideoListRepository

class LoadNewSubtitlesUseCase(
    private val videoListRepository: VideoListRepository
) {
    suspend fun invoke(videoID: Int, subtitles: String) {
        videoListRepository.loadNewSubtitles(videoID = videoID, subtitles = subtitles)
    }
}