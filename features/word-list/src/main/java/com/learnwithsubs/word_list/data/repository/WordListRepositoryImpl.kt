package com.learnwithsubs.word_list.data.repository

import com.learnwithsubs.database.data.storage.WordListDao
import com.learnwithsubs.database.domain.models.WordTranslationDBO
import com.learnwithsubs.word_list.domain.repository.WordListRepository
import kotlinx.coroutines.flow.Flow

class WordListRepositoryImpl(
    private val wordListDao: WordListDao
) : WordListRepository {
    override fun getWords(): Flow<List<WordTranslationDBO>> {
        return wordListDao.getWords()
    }

    override fun getWordsSortedByVideoID(): Flow<List<WordTranslationDBO>> {
        return wordListDao.getWordsSortedByVideoID()
    }

    override suspend fun insertWord(word: WordTranslationDBO) {
        wordListDao.insertWord(word = word)
    }

    override suspend fun deleteWord(word: WordTranslationDBO) {
        wordListDao.deleteWord(word = word)
    }
}