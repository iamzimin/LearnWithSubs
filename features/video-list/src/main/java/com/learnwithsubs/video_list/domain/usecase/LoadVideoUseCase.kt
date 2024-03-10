package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.database.domain.models.Video
import com.learnwithsubs.database.domain.VideoListRepository

class LoadVideoUseCase(
    private val videoListRepository: com.learnwithsubs.database.domain.VideoListRepository
) {

    suspend operator fun invoke(video: com.learnwithsubs.database.domain.models.Video) {
        return videoListRepository.insertVideo(video)
    }
}