package com.learnwithsubs.word_list.domain.usecase

import com.learnwithsubs.database.domain.models.WordTranslation
import com.learnwithsubs.word_list.domain.repository.WordListRepository
import kotlinx.coroutines.flow.Flow

class GetWordsListSortedByVideoIdUseCase(
    private val wordListRepository: WordListRepository
) {
    fun invoke() : Flow<List<WordTranslation>> {
        return wordListRepository.getWordsSortedByVideoID()
    }
}