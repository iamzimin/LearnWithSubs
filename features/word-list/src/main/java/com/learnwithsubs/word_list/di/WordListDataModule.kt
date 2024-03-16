package com.learnwithsubs.word_list.di

import android.content.Context
import androidx.room.Room
import com.learnwithsubs.database.data.storage.WordDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WordListDataModule {

    @Provides
    @Singleton
    fun provideWordDatabase(context: Context) : WordDatabase {
        return Room.databaseBuilder(
            context,
            WordDatabase::class.java,
            WordDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideWordListRepository(db: WordDatabase): com.learnwithsubs.word_list.domain.repository.WordListRepository {
        return com.learnwithsubs.word_list.data.repository.WordListRepositoryImpl(db.wordListDao)
    }

}