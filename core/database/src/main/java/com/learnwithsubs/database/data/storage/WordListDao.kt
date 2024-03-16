package com.learnwithsubs.database.data.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.learnwithsubs.database.domain.models.WordTranslationDBO
import kotlinx.coroutines.flow.Flow


@Dao
interface WordListDao {

    @Query("SELECT * FROM WordTranslationDBO")
    fun getWords(): Flow<List<WordTranslationDBO>>

    @Query("SELECT * FROM WordTranslationDBO ORDER BY videoID")
    fun getWordsSortedByVideoID(): Flow<List<WordTranslationDBO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordTranslationDBO)

    @Delete
    suspend fun deleteWord(word: WordTranslationDBO)

}