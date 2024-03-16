package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.database.domain.models.Video
import com.learnwithsubs.database.domain.VideoListRepository

class DeleteVideoUseCase(
    private val repository: VideoListRepository
) {
    suspend operator fun invoke(video: models.Video) {
        repository.deleteVideo(video)
    }
}