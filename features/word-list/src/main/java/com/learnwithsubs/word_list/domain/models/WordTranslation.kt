package com.learnwithsubs.word_list.domain.models

import com.example.base.Identifiable

data class WordTranslation(
    override val id: Int? = null,
    var word: String,
    var translation: String,
    val nativeLanguage: String,
    val learnLanguage: String,
    val timestamp: Long,
    val videoID: Int? = null,
    val videoName: String? = null,
): Identifiable