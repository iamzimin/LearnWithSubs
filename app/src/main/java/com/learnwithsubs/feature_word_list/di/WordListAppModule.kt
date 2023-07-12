package com.learnwithsubs.feature_word_list.di

import android.content.Context
import com.learnwithsubs.feature_word_list.WordListViewModelFactory
import com.learnwithsubs.feature_word_list.usecase.WordListUseCases
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WordListAppModule(val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideVideoListViewModelFactory(
        wordListUseCases: WordListUseCases
    ): WordListViewModelFactory {
        return WordListViewModelFactory(
            wordListUseCases = wordListUseCases
        )
    }

}