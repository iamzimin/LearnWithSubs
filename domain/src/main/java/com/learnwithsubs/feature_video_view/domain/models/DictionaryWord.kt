package com.learnwithsubs.feature_video_view.domain.models

data class DictionaryWord(
    val id: Int,
    val word: String,
    val translation: String,
    val partSpeech: String
)