package com.learnwithsubs.video_list.data.repository

import com.example.base.VideoConstants
import com.learnwithsubs.database.data.storage.VideoListDao
import com.learnwithsubs.database.domain.models.VideoDBO
import com.learnwithsubs.video_list.domain.repository.VideoListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File

class VideoListRepositoryImpl(
    private val dao: VideoListDao
) : VideoListRepository {
    override fun getVideos(): Flow<List<VideoDBO>> {
        return dao.getVideos()
    }

    override suspend fun getVideoById(id: Int): VideoDBO? {
        return dao.getVideoById(id)
    }

    override suspend fun insertVideo(video: VideoDBO) {
        dao.insertVideo(video)
    }

    override suspend fun deleteVideo(video: VideoDBO) {
        val subSTR = File(video.outputPath)
        if (subSTR.exists())
            subSTR.deleteRecursively()
        dao.deleteVideo(video)
    }

    override suspend fun getLastVideo(): VideoDBO? {
        return dao.getLastVideo()
    }


    override suspend fun saveSubtitles(video: VideoDBO, subtitles: String) {
        val subSTR = File(video.outputPath, VideoConstants.GENERATED_SUBTITLES)
        if (subSTR.exists())
            subSTR.delete()

        withContext(Dispatchers.IO) {
            subSTR.createNewFile()
            val writer = subSTR.bufferedWriter()
            writer.write(subtitles)
            writer.close()
        }
    }

    override suspend fun loadNewSubtitles(video: VideoDBO, subtitles: String) {
        val subSTR = File(video.outputPath, VideoConstants.OWN_SUBTITLES)
        if (subSTR.exists())
            subSTR.delete()

        withContext(Dispatchers.IO) {
            subSTR.createNewFile()
            val writer = subSTR.bufferedWriter()
            writer.write(subtitles)
            writer.close()
        }

        video.apply {
            isOwnSubtitles = true
        }
        insertVideo(video = video)
    }

    override suspend fun backOldSubtitles(video: VideoDBO) {
        video.apply {
            isOwnSubtitles = false
        }
        insertVideo(video = video)
    }
}