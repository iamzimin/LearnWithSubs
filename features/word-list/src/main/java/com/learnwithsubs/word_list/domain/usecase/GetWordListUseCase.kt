package com.learnwithsubs.word_list.domain.usecase

import com.learnwithsubs.database.domain.models.WordTranslation
import com.learnwithsubs.word_list.domain.repository.WordListRepository
import kotlinx.coroutines.flow.Flow

class GetWordListUseCase(
    private val wordListRepository: WordListRepository
) {
    fun invoke() : Flow<List<com.learnwithsubs.database.domain.models.WordTranslation>> {
        return wordListRepository.getWords()
    }
}