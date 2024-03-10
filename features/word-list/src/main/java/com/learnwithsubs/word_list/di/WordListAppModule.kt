package com.learnwithsubs.word_list.di

import android.content.Context
import com.learnwithsubs.word_list.presentation.WordListViewModelFactory
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
        wordListUseCases: com.learnwithsubs.word_list.domain.usecase.WordListUseCases
    ): WordListViewModelFactory {
        return WordListViewModelFactory(
            wordListUseCases = wordListUseCases
        )
    }

}