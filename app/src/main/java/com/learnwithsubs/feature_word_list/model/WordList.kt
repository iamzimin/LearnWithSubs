package com.learnwithsubs.feature_word_list.model

import com.learnwithsubs.feature_word_list.models.WordTranslation

sealed class WordList(open val id: Int?) {
    data class Title(override val id: Int?, val title: String, val videoID: Int?) : WordList(id)
    data class Data(override val id: Int?, val data: WordTranslation) : WordList(id)
}