package com.learnwithsubs.video_view.data.repository

import com.example.base.VideoConstants
import com.learnwithsubs.database.data.storage.VideoListDao
import com.learnwithsubs.database.data.storage.WordListDao
import com.learnwithsubs.database.domain.models.VideoDBO
import com.learnwithsubs.database.domain.models.WordTranslationDBO
import com.learnwithsubs.video_view.domain.models.Subtitle
import com.learnwithsubs.video_view.domain.models.Video
import com.learnwithsubs.video_view.domain.repository.VideoViewRepository
import java.io.File

class VideoViewRepositoryImpl(
    private val videoDao: VideoListDao,
    private val wordDao: WordListDao,
) : VideoViewRepository {
    override fun getVideoSubtitles(video: Video): List<Subtitle>? {
        val subtitlesPath = if (video.isOwnSubtitles)
             File(video.outputPath, VideoConstants.OWN_SUBTITLES)
        else File(video.outputPath, VideoConstants.GENERATED_SUBTITLES)

        var subs = ""
        if (subtitlesPath.exists()) {
            val reader = subtitlesPath.bufferedReader()
            subs = reader.readText()
            reader.close()
        }

        return convertSubtitles(subs)
    }

    override suspend fun getVideoById(videoID: Int): VideoDBO? {
        return videoDao.getVideoById(id = videoID)
    }

    override suspend fun updateVideo(video: Video) {
        videoDao.updateVideo(
            id = video.id,
            name = video.name,
            watchProgress = video.watchProgress,
            saveWords = video.saveWords,
            outputPath = video.outputPath)
    }

    override suspend fun saveWord(word: WordTranslationDBO) {
        wordDao.insertWord(word = word)
    }

    private fun convertSubtitles(subtitles: String): List<Subtitle>? {
        val subtitleLines = subtitles.trim().split("\n\n")
        val subtitleList: List<Subtitle>?
        try {
            subtitleList = subtitleLines.map { subtitleLine ->
                val parts = subtitleLine.split("\n")
                val number = parts[0].toInt()
                val timeRange = parts[1].split(" --> ")
                val startTime = parseTime(timeRange[0])
                val endTime = parseTime(timeRange[1])
                val text = parts[2]
                Subtitle(
                    number,
                    startTime,
                    endTime,
                    text
                )
            }
        }catch (_: Exception) {
            return null
        }

        return subtitleList
    }

    private fun parseTime(timeString: String): Long {
        val parts = timeString.split("[:,]".toRegex()).toTypedArray()

        val hours = parts[0].toLong()
        val minutes = parts[1].toLong()
        val seconds = parts[2].toLong()
        val milliseconds = parts[3].toLong()

        return (hours * 3600000) + (minutes * 60000) + (seconds * 1000) + milliseconds
    }
}