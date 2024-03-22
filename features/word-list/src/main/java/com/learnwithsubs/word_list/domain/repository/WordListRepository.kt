package com.learnwithsubs.word_list.domain.repository

import com.learnwithsubs.database.domain.models.WordTranslationDBO
import kotlinx.coroutines.flow.Flow

interface WordListRepository {
    fun getWords(): Flow<List<WordTranslationDBO>>
    fun getWordsSortedByVideoID(): Flow<List<WordTranslationDBO>>
    suspend fun insertWord(word: WordTranslationDBO)
    suspend fun deleteWord(word: WordTranslationDBO)
}