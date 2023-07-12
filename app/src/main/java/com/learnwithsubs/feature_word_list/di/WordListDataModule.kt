package com.learnwithsubs.feature_word_list.di

import android.content.Context
import androidx.room.Room
import com.learnwithsubs.feature_video_list.repository.VideoListRepository
import com.learnwithsubs.feature_video_list.repository.VideoListRepositoryImpl
import com.learnwithsubs.feature_video_list.storage.VideoDatabase
import com.learnwithsubs.feature_word_list.repository.WordListRepository
import com.learnwithsubs.feature_word_list.repository.WordListRepositoryImpl
import com.learnwithsubs.feature_word_list.storage.WordDatabase
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
    fun provideWordListRepository(db: WordDatabase): WordListRepository {
        return WordListRepositoryImpl(db.wordListDao)
    }

}