package com.learnwithsubs.word_list.domain.usecase

import com.learnwithsubs.database.domain.models.WordTranslation
import com.learnwithsubs.word_list.domain.repository.WordListRepository

class LoadWordUseCase(
    private val wordListRepository: WordListRepository
) {
    suspend fun invoke(word: WordTranslation) {
        wordListRepository.insertWord(word = word)
    }
}