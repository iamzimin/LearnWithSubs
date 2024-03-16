package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.database.domain.models.Video
import com.learnwithsubs.database.domain.VideoListRepository
import com.learnwithsubs.video_list.domain.repository.VideoListRepository

class LoadNewSubtitlesUseCase(
    private val videoListRepository: VideoListRepository
) {
    suspend fun invoke(video: Video, subtitles: String) {
        videoListRepository.loadNewSubtitles(video = video, subtitles = subtitles)
    }
}