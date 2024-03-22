package com.learnwithsubs.word_list.domain.usecase

import com.learnwithsubs.word_list.domain.models.WordTranslation
import com.learnwithsubs.word_list.domain.repository.WordListRepository
import com.learnwithsubs.word_list.domain.toWordTranslation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWordsListSortedByVideoIdUseCase(
    private val wordListRepository: WordListRepository
) {
    fun invoke() : Flow<List<WordTranslation>> {
        return wordListRepository.getWordsSortedByVideoID().map { wordDBOList ->
            wordDBOList.map { wordDBO ->
                wordDBO.toWordTranslation()
            }
        }
    }
}