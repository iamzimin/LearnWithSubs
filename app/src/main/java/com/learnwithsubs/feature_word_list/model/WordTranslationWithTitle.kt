package com.learnwithsubs.feature_word_list.model

import com.learnwithsubs.feature_word_list.models.WordTranslation

data class WordTranslationWithTitle(
    val title: String,
    val listWords: List<WordTranslation>,
)
