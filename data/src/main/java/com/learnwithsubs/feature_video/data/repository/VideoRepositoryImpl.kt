package com.learnwithsubs.feature_video.data.repository

import com.learnwithsubs.feature_video.data.storage.VideoDao
import com.learnwithsubs.feature_video.domain.models.Video
import com.learnwithsubs.feature_video.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow

class VideoRepositoryImpl(
    private val dao: VideoDao
) : VideoRepository {
    override fun getVideos(): Flow<List<Video>> {
        return dao.getVideos()
    }

    override suspend fun getVideoById(id: Int): Video? {
        return dao.getNoteById(id)
    }

    override suspend fun insertVideo(video: Video) {
        return dao.insertVideo(video)
    }

    override suspend fun deleteVideo(video: Video) {
        return dao.deleteVideo(video)
    }
}