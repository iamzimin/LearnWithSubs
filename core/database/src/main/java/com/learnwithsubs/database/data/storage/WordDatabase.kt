package com.learnwithsubs.database.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.learnwithsubs.database.domain.models.WordTranslationDBO

@Database(
    entities = [WordTranslationDBO::class],
    version = 1
)
abstract class WordDatabase : RoomDatabase() {
    abstract val wordListDao: WordListDao

    companion object {
        const val DATABASE_NAME = "words_db"
    }
}