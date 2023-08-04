package com.learnwithsubs.feature_word_list.repository

import com.learnwithsubs.feature_word_list.models.WordTranslation
import kotlinx.coroutines.flow.Flow

interface WordListRepository {
    fun getWords(): Flow<List<WordTranslation>>
    fun getWordsSortedByVideoID(): Flow<List<WordTranslation>>
    suspend fun insertWord(word: WordTranslation)
    suspend fun deleteWord(word: WordTranslation)
}