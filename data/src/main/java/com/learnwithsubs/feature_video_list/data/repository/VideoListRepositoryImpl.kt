package com.learnwithsubs.feature_video_list.data.repository

import com.learnwithsubs.feature_video_list.data.storage.VideoListDao
import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.repository.VideoListRepository
import kotlinx.coroutines.flow.Flow

class VideoListRepositoryImpl(
    private val dao: VideoListDao
) : VideoListRepository {
    override fun getVideos(): Flow<List<Video>> {
        return dao.getVideos()
    }

    override suspend fun getVideoById(id: Int): Video? {
        return dao.getVideoById(id)
    }

    override suspend fun insertVideo(video: Video) {
        return dao.insertVideo(video)
    }

    override suspend fun deleteVideo(video: Video) {
        return dao.deleteVideo(video)
    }
}