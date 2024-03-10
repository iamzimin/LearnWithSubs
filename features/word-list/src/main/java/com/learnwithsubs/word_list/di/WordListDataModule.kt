package com.learnwithsubs.word_list.di

import android.content.Context
import androidx.room.Room
import com.learnwithsubs.word_list.domain.repository.WordListRepository
import com.learnwithsubs.word_list.data.repository.WordListRepositoryImpl
import com.learnwithsubs.word_list.data.storage.WordDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WordListDataModule {

    @Provides
    @Singleton
    fun provideWordDatabase(context: Context) : com.learnwithsubs.word_list.data.storage.WordDatabase {
        return Room.databaseBuilder(
            context,
            com.learnwithsubs.word_list.data.storage.WordDatabase::class.java,
            com.learnwithsubs.word_list.data.storage.WordDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideWordListRepository(db: com.learnwithsubs.word_list.data.storage.WordDatabase): com.learnwithsubs.word_list.domain.repository.WordListRepository {
        return com.learnwithsubs.word_list.data.repository.WordListRepositoryImpl(db.wordListDao)
    }

}