package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.database.domain.models.Video
import com.learnwithsubs.database.domain.VideoListRepository
import kotlinx.coroutines.flow.Flow

class GetVideoListUseCase(
    private val repository: com.learnwithsubs.database.domain.VideoListRepository
) {
    fun invoke() : Flow<List<com.learnwithsubs.database.domain.models.Video>> {
        return repository.getVideos()
    }
}