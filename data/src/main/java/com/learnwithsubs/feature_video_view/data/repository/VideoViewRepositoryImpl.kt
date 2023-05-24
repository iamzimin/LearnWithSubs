package com.learnwithsubs.feature_video_view.data.repository

import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_view.data.storage.VideoViewDao
import com.learnwithsubs.feature_video_view.domain.repository.VideoViewRepository

class VideoViewRepositoryImpl(
    private val dao: VideoViewDao
) : VideoViewRepository {
    override fun getVideoSubtitles(): String {
        return dao.getSubtitles()
    }

    override suspend fun updateVideo(video: Video) {
        return dao.updateVideo(video)
    }
}