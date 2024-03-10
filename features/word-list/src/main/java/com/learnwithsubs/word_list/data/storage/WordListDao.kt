package com.learnwithsubs.word_list.data.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.learnwithsubs.word_list.domain.models.WordTranslation
import kotlinx.coroutines.flow.Flow


@Dao
interface WordListDao {

    @Query("SELECT * FROM WordTranslation")
    fun getWords(): Flow<List<com.learnwithsubs.word_list.domain.models.WordTranslation>>

    @Query("SELECT * FROM WordTranslation ORDER BY videoID")
    fun getWordsSortedByVideoID(): Flow<List<com.learnwithsubs.word_list.domain.models.WordTranslation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: com.learnwithsubs.word_list.domain.models.WordTranslation)

    @Delete
    suspend fun deleteWord(word: com.learnwithsubs.word_list.domain.models.WordTranslation)

}