package com.learnwithsubs.word_list.di

import android.content.Context
import androidx.room.Room
import com.learnwithsubs.database.data.storage.WordDatabase
import com.learnwithsubs.shared_preference_settings.data.repository.SharedPreferenceSettingsImpl
import com.learnwithsubs.shared_preference_settings.domain.repository.SharedPreferenceSettings
import com.learnwithsubs.word_list.data.repository.WordListRepositoryImpl
import com.learnwithsubs.word_list.domain.repository.WordListRepository
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

    @Provides
    @Singleton
    fun provideSharedPreferenceSettingsRepository(context: Context): SharedPreferenceSettings {
        return SharedPreferenceSettingsImpl(context)
    }

}