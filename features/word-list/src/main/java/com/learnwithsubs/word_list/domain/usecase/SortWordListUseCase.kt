package com.learnwithsubs.word_list.domain.usecase

import com.learnwithsubs.database.domain.models.WordTranslation
import com.learnwithsubs.word_list.domain.util.WordOrder
import com.learnwithsubs.general.util.OrderType

class SortWordListUseCase {
    operator fun invoke(wordList: ArrayList<com.learnwithsubs.database.domain.models.WordTranslation>, sortMode: WordOrder, filter: String?): List<com.learnwithsubs.database.domain.models.WordTranslation> {
        val sorted = when (sortMode.orderType) {
            is OrderType.Ascending -> {
                when (sortMode) {
                    is WordOrder.Video -> wordList.sortedBy { it.videoID }
                    is WordOrder.Alphabet -> wordList.sortedBy { it.translation.lowercase() } // is WordOrder.Alphabet -> wordList.sortedWith(compareBy({ it.videoID }, { it.translation.lowercase() }))
                    is WordOrder.Date -> wordList.sortedBy { it.timestamp }
                }
            }
            is OrderType.Descending -> {
                when (sortMode) {
                    is WordOrder.Video -> wordList.sortedByDescending { it.videoID }
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