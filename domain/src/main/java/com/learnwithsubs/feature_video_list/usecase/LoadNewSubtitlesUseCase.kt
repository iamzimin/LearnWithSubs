package com.learnwithsubs.feature_video_list.usecase

import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.repository.VideoListRepository

class LoadNewSubtitlesUseCase(
    private val videoListRepository: VideoListRepository
) {
    suspend fun invoke(video: Video, subtitles: String) {
        videoListRepository.loadNewSubtitles(video = video, subtitles = subtitles)
    }
}