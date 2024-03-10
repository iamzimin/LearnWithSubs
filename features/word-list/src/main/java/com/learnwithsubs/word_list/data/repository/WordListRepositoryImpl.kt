package com.learnwithsubs.word_list.data.repository

import com.learnwithsubs.word_list.domain.models.WordTranslation
import com.learnwithsubs.word_list.data.storage.WordListDao
import kotlinx.coroutines.flow.Flow

class WordListRepositoryImpl(
    private val wordListDao: WordListDao
) : com.learnwithsubs.word_list.domain.repository.WordListRepository {
    override fun getWords(): Flow<List<com.learnwithsubs.word_list.domain.models.WordTranslation>> {
        return wordListDao.getWords()
    }

    override fun getWordsSortedByVideoID(): Flow<List<com.learnwithsubs.word_list.domain.models.WordTranslation>> {
        return wordListDao.getWordsSortedByVideoID()
    }

    override suspend fun insertWord(word: com.learnwithsubs.word_list.domain.models.WordTranslation) {
        wordListDao.insertWord(word = word)
    }

    override suspend fun deleteWord(word: com.learnwithsubs.word_list.domain.models.WordTranslation) {
        wordListDao.deleteWord(word = word)
    }
}