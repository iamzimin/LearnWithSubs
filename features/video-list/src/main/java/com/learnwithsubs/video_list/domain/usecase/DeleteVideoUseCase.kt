package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.video_list.domain.models.Video
import com.learnwithsubs.video_list.domain.repository.VideoListRepository
import com.learnwithsubs.video_list.domain.toVideoDBO

class DeleteVideoUseCase(
    private val repository: VideoListRepository
) {
    suspend operator fun invoke(video: Video) {
        repository.deleteVideo(video.toVideoDBO())
    }
}