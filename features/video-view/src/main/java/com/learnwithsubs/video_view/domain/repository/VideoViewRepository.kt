package com.learnwithsubs.video_view.domain.repository

import com.learnwithsubs.database.domain.models.VideoDBO
import com.learnwithsubs.database.domain.models.WordTranslationDBO
import com.learnwithsubs.video_view.domain.models.Subtitle
import com.learnwithsubs.video_view.domain.models.Video

interface VideoViewRepository {
    fun getVideoSubtitles(video: Video): List<Subtitle>?
    suspend fun getVideoById(videoID: Int): VideoDBO?
    suspend fun updateVideo(video: Video)
    suspend fun saveWord(word: WordTranslationDBO)
}