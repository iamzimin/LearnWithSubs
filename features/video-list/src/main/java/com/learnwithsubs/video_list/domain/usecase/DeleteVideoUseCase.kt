package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.database.domain.models.Video
import com.learnwithsubs.database.domain.VideoListRepository

class DeleteVideoUseCase(
    private val repository: com.learnwithsubs.database.domain.VideoListRepository
) {
    suspend operator fun invoke(video: com.learnwithsubs.database.domain.models.Video) {
        repository.deleteVideo(video)
    }
}