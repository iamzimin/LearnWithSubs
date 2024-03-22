package com.learnwithsubs.video_view.domain.repository

import com.learnwithsubs.database.domain.models.VideoDBO
import com.learnwithsubs.database.domain.models.WordTranslationDBO
import com.learnwithsubs.video_view.domain.models.Subtitle

interface VideoViewRepository {
    fun getVideoSubtitles(video: VideoDBO): List<Subtitle>?
    suspend fun updateVideo(video: VideoDBO)
    suspend fun saveWord(word: WordTranslationDBO)
}