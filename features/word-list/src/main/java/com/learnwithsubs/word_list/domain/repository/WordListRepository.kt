package com.learnwithsubs.word_list.domain.repository

import com.learnwithsubs.database.domain.models.WordTranslation
import kotlinx.coroutines.flow.Flow

interface WordListRepository {
    fun getWords(): Flow<List<com.learnwithsubs.database.domain.models.WordTranslation>>
    fun getWordsSortedByVideoID(): Flow<List<com.learnwithsubs.database.domain.models.WordTranslation>>
    suspend fun insertWord(word: com.learnwithsubs.database.domain.models.WordTranslation)
    suspend fun deleteWord(word: com.learnwithsubs.database.domain.models.WordTranslation)
}