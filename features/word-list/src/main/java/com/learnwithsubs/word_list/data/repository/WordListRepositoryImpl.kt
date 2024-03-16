package com.learnwithsubs.word_list.data.repository

import com.learnwithsubs.database.data.storage.WordListDao
import kotlinx.coroutines.flow.Flow

class WordListRepositoryImpl(
    private val wordListDao: WordListDao
) : com.learnwithsubs.word_list.domain.repository.WordListRepository {
    override fun getWords(): Flow<List<WordTranslation>> {
        return wordListDao.getWords()
    }

    override fun getWordsSortedByVideoID(): Flow<List<WordTranslation>> {
        return wordListDao.getWordsSortedByVideoID()
    }

    override suspend fun insertWord(word: WordTranslation) {
        wordListDao.insertWord(word = word)
    }

    override suspend fun deleteWord(word: WordTranslation) {
        wordListDao.deleteWord(word = word)
    }
}