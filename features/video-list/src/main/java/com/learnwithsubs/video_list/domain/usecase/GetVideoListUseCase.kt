package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.database.domain.models.Video
import com.learnwithsubs.database.domain.VideoListRepository
import kotlinx.coroutines.flow.Flow

class GetVideoListUseCase(
    private val repository: VideoListRepository
) {
    fun invoke() : Flow<List<Video>> {
        return repository.getVideos()
    }
}