package com.learnwithsubs.word_list.domain.usecase

import com.learnwithsubs.database.domain.models.WordTranslation

class FilterWordListUseCase {
    operator fun invoke(wordList: ArrayList<com.learnwithsubs.database.domain.models.WordTranslation>, filter: String?): List<com.learnwithsubs.database.domain.models.WordTranslation> {
        val filtered = wordList.filter { word ->
            filter?.let {
                word.translation.lowercase().contains(it.lowercase())
            } ?: true
        }
        return filtered
    }
}