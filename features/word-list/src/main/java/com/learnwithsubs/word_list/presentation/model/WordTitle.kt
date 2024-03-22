package com.learnwithsubs.word_list.presentation.model

import com.learnwithsubs.word_list.domain.models.WordTranslation

data class WordTitle(
    val id: Int?,
    val title: String?,
    val wordList: ArrayList<WordTranslation>,
)