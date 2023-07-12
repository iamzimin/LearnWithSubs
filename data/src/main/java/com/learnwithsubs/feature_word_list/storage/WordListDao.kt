package com.learnwithsubs.feature_word_list.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.learnwithsubs.feature_word_list.models.WordTranslation
import kotlinx.coroutines.flow.Flow


@Dao
interface WordListDao {

    @Query("SELECT * FROM WordTranslation")
    fun getWords(): Flow<List<WordTranslation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordTranslation)

    @Delete
    suspend fun deleteWord(word: WordTranslation)

}