package com.learnwithsubs.word_list.di

import com.learnwithsubs.word_list.domain.repository.WordListRepository
import com.learnwithsubs.word_list.domain.usecase.DeleteWordUseCase
import com.learnwithsubs.word_list.domain.usecase.FilterWordListUseCase
import com.learnwithsubs.word_list.domain.usecase.GetWordListUseCase
import com.learnwithsubs.word_list.domain.usecase.GetWordsListSortedByVideoIdUseCase
import com.learnwithsubs.word_list.domain.usecase.LoadWordUseCase
import com.learnwithsubs.word_list.domain.usecase.SortWordListUseCase
import com.learnwithsubs.word_list.domain.usecase.WordListUseCases
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WordListDomainModule {

    @Provides
    @Singleton
    fun provideVideoListUseCases(
        wordListRepository: com.learnwithsubs.word_list.domain.repository.WordListRepository
    ): com.learnwithsubs.word_list.domain.usecase.WordListUseCases {
        return com.learnwithsubs.word_list.domain.usecase.WordListUseCases(
            getWordListUseCase = com.learnwithsubs.word_list.domain.usecase.GetWordListUseCase(
                wordListRepository
            ),
            loadWordUseCase = com.learnwithsubs.word_list.domain.usecase.LoadWordUseCase(
                wordListRepository
            ),
            deleteWordUseCase = com.learnwithsubs.word_list.domain.usecase.DeleteWordUseCase(
                wordListRepository
            ),
            sortWordListUseCases = com.learnwithsubs.word_list.domain.usecase.SortWordListUseCase(),
            filterWordListUseCase = com.learnwithsubs.word_list.domain.usecase.FilterWordListUseCase(),
            getWordsListSortedByVideoIdUseCase = com.learnwithsubs.word_list.domain.usecase.GetWordsListSortedByVideoIdUseCase(
                wordListRepository
            ),
        )
    }
}