package com.learnwithsubs.feature_word_list.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.learnwithsubs.feature_word_list.models.WordTranslation

@Database(
    entities = [WordTranslation::class],
    version = 1
)
abstract class WordDatabase : RoomDatabase() {
    abstract val wordListDao: WordListDao

    companion object {
        const val DATABASE_NAME = "words_db"
    }
}