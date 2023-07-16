package com.learnwithsubs.feature_word_list.model

import com.learnwithsubs.feature_word_list.models.WordTranslation
import com.learnwithsubs.general.models.Identifiable

data class WordTranslationWithTitle(
    override val id: Int?,
    val title: String,
    var listWords: ArrayList<WordTranslation>,
) : Identifiable