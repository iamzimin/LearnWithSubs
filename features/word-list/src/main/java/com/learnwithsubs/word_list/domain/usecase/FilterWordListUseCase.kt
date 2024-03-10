package com.learnwithsubs.word_list.domain.usecase

import com.learnwithsubs.word_list.domain.models.WordTranslation

class FilterWordListUseCase {
    operator fun invoke(wordList: ArrayList<WordTranslation>, filter: String?): List<WordTranslation> {
        val filtered = wordList.filter { word ->
            filter?.let {
                word.translation.lowercase().contains(it.lowercase())
            } ?: true
        }
        return filtered
    }
}