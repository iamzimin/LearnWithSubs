package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.database.domain.models.Video
import com.learnwithsubs.database.domain.VideoListRepository

class GetVideoUseCase(
    private val videoListRepository: com.learnwithsubs.database.domain.VideoListRepository
) {

    suspend operator fun invoke(id: Int): com.learnwithsubs.database.domain.models.Video? {
        return videoListRepository.getVideoById(id = id)
    }
}