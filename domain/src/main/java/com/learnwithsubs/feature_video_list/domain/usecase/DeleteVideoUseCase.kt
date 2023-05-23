package com.learnwithsubs.feature_video_list.domain.usecase

import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.repository.VideoRepository

class DeleteVideoUseCase(
    private val repository: VideoRepository
) {
    suspend operator fun invoke(video: Video) {
        repository.deleteVideo(video)
    }
}