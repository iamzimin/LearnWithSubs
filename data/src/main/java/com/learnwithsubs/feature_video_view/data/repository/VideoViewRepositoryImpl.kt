package com.learnwithsubs.feature_video_view.data.repository

import android.content.Context
import android.widget.Toast
import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_view.data.storage.VideoViewDao
import com.learnwithsubs.feature_video_view.domain.models.Subtitle
import com.learnwithsubs.feature_video_view.domain.repository.VideoViewRepository
import java.io.File

class VideoViewRepositoryImpl(
    private val dao: VideoViewDao,
    private val context: Context,
) : VideoViewRepository {
    override fun getVideoSubtitles(video: Video?): List<Subtitle> {
        if (video == null) {
            Toast.makeText(context.applicationContext, "Video = null", Toast.LENGTH_SHORT).show() //TODO
            return emptyList()
        }

        val internalStorageDir = File(context.filesDir, "LearnWithSubs/${video.id}.srt")
        var subs = ""
        if (internalStorageDir.exists()) {
            val reader = internalStorageDir.bufferedReader()
            subs = reader.readText()
            reader.close()
        }
        val converted = convertSubtitles(subs)

        return converted
    }

    override suspend fun updateVideo(video: Video) {
        return dao.updateVideo(video)
    }

    private fun convertSubtitles(subtitles: String): List<Subtitle> {
        val subtitleLines = subtitles.split("\n\n")

        val subtitleList = subtitleLines.map { subtitleLine ->
            val parts = subtitleLine.split("\n")
            val number = parts[0].toInt()
            val timeRange = parts[1].split(" --> ")
            val startTime = parseTime(timeRange[0])
            val endTime = parseTime(timeRange[1])
            val text = parts[2]
            Subtitle(number, startTime, endTime, text)
        }
        return subtitleList
    }

    private fun parseTime(timeString: String): Long {
        val parts = timeString.split("[:,]".toRegex()).toTypedArray()

        val hours = parts[0].toLong()
        val minutes = parts[1].toLong()
        val seconds = parts[2].toLong()
        val milliseconds = parts[3].toLong()

        val totalMilliseconds = (hours * 3600000) + (minutes * 60000) + (seconds * 1000) + milliseconds
        return totalMilliseconds
    }
}