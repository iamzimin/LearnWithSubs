package com.learnwithsubs.word_list.presentation.model

import com.learnwithsubs.database.domain.models.WordTranslation

data class WordTitle(
    val id: Int?,
    val title: String?,
    val wordList: ArrayList<com.learnwithsubs.database.domain.models.WordTranslation>,
)