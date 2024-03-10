package com.learnwithsubs.word_list.domain.repository

import com.learnwithsubs.word_list.domain.models.WordTranslation
import kotlinx.coroutines.flow.Flow

interface WordListRepository {
    fun getWords(): Flow<List<WordTranslation>>
    fun getWordsSortedByVideoID(): Flow<List<WordTranslation>>
    suspend fun insertWord(word: WordTranslation)
    suspend fun deleteWord(word: WordTranslation)
}