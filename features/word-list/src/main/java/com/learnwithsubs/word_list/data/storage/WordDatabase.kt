package com.learnwithsubs.word_list.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.learnwithsubs.word_list.domain.models.WordTranslation

@Database(
    entities = [com.learnwithsubs.word_list.domain.models.WordTranslation::class],
    version = 1
)
abstract class WordDatabase : RoomDatabase() {
    abstract val wordListDao: WordListDao

    companion object {
        const val DATABASE_NAME = "words_db"
    }
}