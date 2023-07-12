package com.learnwithsubs.feature_word_list.usecase

import com.learnwithsubs.feature_word_list.models.WordTranslation
import com.learnwithsubs.feature_word_list.repository.WordListRepository

class DeleteWordUseCase(
    private val wordListRepository: WordListRepository
) {
    suspend fun invoke(word: WordTranslation) {
        wordListRepository.deleteWord(word = word)
    }
}