package com.learnwithsubs.feature_word_list.model

import com.learnwithsubs.feature_word_list.models.WordTranslation

data class WordTitle(
    val id: Int?,
    val title: String?,
    val wordList: ArrayList<WordTranslation>,
)