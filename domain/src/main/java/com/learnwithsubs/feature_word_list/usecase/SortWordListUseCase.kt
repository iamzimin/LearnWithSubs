package com.learnwithsubs.feature_word_list.usecase

import com.learnwithsubs.feature_word_list.models.WordTranslation
import com.learnwithsubs.feature_word_list.util.WordOrder
import com.learnwithsubs.general.util.OrderType

class SortWordListUseCase {
    operator fun invoke(wordList: ArrayList<WordTranslation>, sortMode: WordOrder, filter: String?): List<WordTranslation> {
        val sorted = when (sortMode.orderType) {
            is OrderType.Ascending -> {
                when (sortMode) {
                    is WordOrder.Video -> wordList.sortedBy { it.videoName?.lowercase() }
                    is WordOrder.Alphabet -> wordList.sortedBy { it.translation.lowercase() }
                    is WordOrder.Date -> wordList.sortedBy { it.timestamp }
                }
            }
            is OrderType.Descending -> {
                when (sortMode) {
                    is WordOrder.Video -> wordList.sortedByDescending { it.videoName?.lowercase() }
                    is WordOrder.Alphabet -> wordList.sortedByDescending { it.translation.lowercase() }
                    is WordOrder.Date -> wordList.sortedByDescending { it.timestamp }
                }
            }
        }.filter { word ->
            filter?.let {
                word.translation.lowercase().contains(it.lowercase())
            } ?: true
        }
        return sorted
    }
}