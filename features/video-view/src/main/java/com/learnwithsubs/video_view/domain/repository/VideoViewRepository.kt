package com.learnwithsubs.video_view.domain.repository

import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.video_view.domain.models.Subtitle
import com.learnwithsubs.word_list.domain.models.WordTranslation

interface VideoViewRepository {
    fun getVideoSubtitles(video: Video): List<Subtitle>?

    suspend fun updateVideo(video: Video)
    suspend fun saveWord(word: com.learnwithsubs.word_list.domain.models.WordTranslation)
}