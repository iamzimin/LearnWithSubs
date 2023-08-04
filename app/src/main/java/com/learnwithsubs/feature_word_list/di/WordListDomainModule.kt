package com.learnwithsubs.feature_word_list.di

import com.learnwithsubs.feature_word_list.repository.WordListRepository
import com.learnwithsubs.feature_word_list.usecase.DeleteWordUseCase
import com.learnwithsubs.feature_word_list.usecase.FilterWordListUseCase
import com.learnwithsubs.feature_word_list.usecase.GetWordListUseCase
import com.learnwithsubs.feature_word_list.usecase.GetWordsListSortedByVideoIdUseCase
import com.learnwithsubs.feature_word_list.usecase.LoadWordUseCase
import com.learnwithsubs.feature_word_list.usecase.SortWordListUseCase
import com.learnwithsubs.feature_word_list.usecase.WordListUseCases
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WordListDomainModule {

    @Provides
    @Singleton
    fun provideVideoListUseCases(
        wordListRepository: WordListRepository
    ): WordListUseCases {
        return WordListUseCases(
            getWordListUseCase = GetWordListUseCase(wordListRepository),
            loadWordUseCase = LoadWordUseCase(wordListRepository),
            deleteWordUseCase = DeleteWordUseCase(wordListRepository),
            sortWordListUseCases = SortWordListUseCase(),
            filterWordListUseCase = FilterWordListUseCase(),
            getWordsListSortedByVideoIdUseCase = GetWordsListSortedByVideoIdUseCase(wordListRepository),
        )
    }
}