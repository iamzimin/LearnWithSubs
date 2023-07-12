package com.learnwithsubs.feature_video_list.usecase

import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.repository.VideoListRepository
import kotlinx.coroutines.flow.Flow

class GetVideoListUseCase(
    private val repository: VideoListRepository
) {
    fun invoke() : Flow<List<Video>> {
        return repository.getVideos()
    }
}