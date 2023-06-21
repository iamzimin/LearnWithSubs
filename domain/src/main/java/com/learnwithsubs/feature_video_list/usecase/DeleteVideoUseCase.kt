package com.learnwithsubs.feature_video_list.usecase

import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.repository.VideoListRepository

class DeleteVideoUseCase(
    private val repository: VideoListRepository
) {
    suspend operator fun invoke(video: Video) {
        repository.deleteVideo(video)
    }
}