package com.learnwithsubs.word_list.domain.usecase

import com.learnwithsubs.word_list.domain.models.WordTranslation
import com.learnwithsubs.word_list.domain.repository.WordListRepository
import com.learnwithsubs.word_list.domain.toWordTranslationDBO

class DeleteWordUseCase(
    private val wordListRepository: WordListRepository
) {
    suspend fun invoke(word: WordTranslation) {
        wordListRepository.deleteWord(word = word.toWordTranslationDBO())
    }
}