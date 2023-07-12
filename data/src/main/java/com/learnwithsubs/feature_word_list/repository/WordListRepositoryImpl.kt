package com.learnwithsubs.feature_word_list.repository

import com.learnwithsubs.feature_word_list.models.WordTranslation
import com.learnwithsubs.feature_word_list.storage.WordListDao
import kotlinx.coroutines.flow.Flow

class WordListRepositoryImpl(
    private val wordListDao: WordListDao
) : WordListRepository {
    override fun getWords(): Flow<List<WordTranslation>> {
        return wordListDao.getWords()
    }

    override suspend fun insertWord(word: WordTranslation) {
        wordListDao.insertWord(word = word)
    }

    override suspend fun deleteWord(word: WordTranslation) {
        wordListDao.deleteWord(word = word)
    }
}