package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.database.domain.models.Video
import com.learnwithsubs.database.domain.VideoListRepository

class BackOldSubtitlesUseCase(
    private val videoListRepository: com.learnwithsubs.database.domain.VideoListRepository
) {
    suspend fun invoke(video: com.learnwithsubs.database.domain.models.Video) {
        videoListRepository.backOldSubtitles(video = video)
    }
}