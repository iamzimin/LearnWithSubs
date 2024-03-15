package com.learnwithsubs.word_list.data.repository

import com.learnwithsubs.database.data.storage.WordListDao
import kotlinx.coroutines.flow.Flow

class WordListRepositoryImpl(
    private val wordListDao: com.learnwithsubs.database.data.storage.WordListDao
) : com.learnwithsubs.word_list.domain.repository.WordListRepository {
    override fun getWords(): Flow<List<com.learnwithsubs.database.domain.models.WordTranslation>> {
        return wordListDao.getWords()
    }

    override fun getWordsSortedByVideoID(): Flow<List<com.learnwithsubs.database.domain.models.WordTranslation>> {
        return wordListDao.getWordsSortedByVideoID()
    }

    override suspend fun insertWord(word: com.learnwithsubs.database.domain.models.WordTranslation) {
        wordListDao.insertWord(word = word)
    }

    override suspend fun deleteWord(word: com.learnwithsubs.database.domain.models.WordTranslation) {
        wordListDao.deleteWord(word = word)
    }
}