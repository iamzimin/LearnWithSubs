package com.learnwithsubs.word_list.presentation.model

import com.learnwithsubs.word_list.domain.models.WordTranslation

sealed class WordList(open val id: Int?) {
    data class Title(override val id: Int?, val title: String?, val videoID: Int?) : WordList(id)
    data class Data(override val id: Int?, val data: WordTranslation) : WordList(id)
}