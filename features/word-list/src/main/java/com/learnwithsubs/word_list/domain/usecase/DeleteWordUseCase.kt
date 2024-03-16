package com.learnwithsubs.word_list.domain.usecase

import com.learnwithsubs.database.domain.models.WordTranslation
import com.learnwithsubs.word_list.domain.repository.WordListRepository

class DeleteWordUseCase(
    private val wordListRepository: WordListRepository
) {
    suspend fun invoke(word: com.learnwithsubs.database.domain.models.WordTranslation) {
        wordListRepository.deleteWord(word = word)
    }
}