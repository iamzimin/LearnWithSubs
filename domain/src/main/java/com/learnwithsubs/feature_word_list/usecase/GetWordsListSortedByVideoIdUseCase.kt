package com.learnwithsubs.feature_word_list.usecase

import com.learnwithsubs.feature_word_list.models.WordTranslation
import com.learnwithsubs.feature_word_list.repository.WordListRepository
import kotlinx.coroutines.flow.Flow

class GetWordsListSortedByVideoIdUseCase(
    private val wordListRepository: WordListRepository
) {
    fun invoke() : Flow<List<WordTranslation>> {
        return wordListRepository.getWordsSortedByVideoID()
    }
}