package com.learnwithsubs.feature_word_list.usecase

import com.learnwithsubs.feature_word_list.models.WordTranslation

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