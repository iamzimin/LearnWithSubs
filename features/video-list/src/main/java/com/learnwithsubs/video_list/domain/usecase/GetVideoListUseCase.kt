package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.video_list.domain.models.Video
import com.learnwithsubs.video_list.domain.repository.VideoListRepository
import com.learnwithsubs.video_list.domain.toVideo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetVideoListUseCase(
    private val repository: VideoListRepository
) {
    fun invoke(): Flow<List<Video>> {
        return repository.getVideos().map { videoDBOList ->
            videoDBOList.map { videoDBO ->
                videoDBO.toVideo()
            }
        }
    }
}