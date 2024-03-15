package com.learnwithsubs.video_list.data.repository

import com.learnwithsubs.database.data.storage.VideoListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File

class VideoListRepositoryImpl(
    private val dao: com.learnwithsubs.database.data.storage.VideoListDao
) : com.learnwithsubs.database.domain.VideoListRepository {
    override fun getVideos(): Flow<List<com.learnwithsubs.database.domain.models.Video>> {
        return dao.getVideos()
    }

    override suspend fun getVideoById(id: Int): com.learnwithsubs.database.domain.models.Video? {
        return dao.getVideoById(id)
    }

    override suspend fun insertVideo(video: com.learnwithsubs.database.domain.models.Video) {
        return dao.insertVideo(video)
    }

    override suspend fun deleteVideo(video: com.learnwithsubs.database.domain.models.Video) {
        return dao.deleteVideo(video)
    }

    override suspend fun getLastVideo(): com.learnwithsubs.database.domain.models.Video? {
        return dao.getLastVideo()
    }


    override suspend fun saveSubtitles(video: com.learnwithsubs.database.domain.models.Video, subtitles: String) {
        val subSTR = File(video.outputPath, com.example.yandex_dictionary_api.domain.VideoConstants.GENERATED_SUBTITLES)
        if (subSTR.exists())
            subSTR.delete()

        withContext(Dispatchers.IO) {
            subSTR.createNewFile()
            val writer = subSTR.bufferedWriter()
            writer.write(subtitles)
            writer.close()
        }
    }

    override suspend fun loadNewSubtitles(video: com.learnwithsubs.database.domain.models.Video, subtitles: String) {
        val subSTR = File(video.outputPath, com.example.yandex_dictionary_api.domain.VideoConstants.OWN_SUBTITLES)
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

    override suspend fun backOldSubtitles(video: com.learnwithsubs.database.domain.models.Video) {
        video.apply {
            isOwnSubtitles = false
        }
        insertVideo(video = video)
    }
}