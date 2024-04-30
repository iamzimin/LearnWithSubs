package com.learnwithsubs.word_list.di

import com.learnwithsubs.shared_preference_settings.domain.repository.SharedPreferenceSettings
import com.learnwithsubs.word_list.domain.repository.WordListRepository
import com.learnwithsubs.word_list.domain.usecase.DeleteWordUseCase
import com.learnwithsubs.word_list.domain.usecase.FilterWordListUseCase
import com.learnwithsubs.word_list.domain.usecase.GetLearningLanguage
import com.learnwithsubs.word_list.domain.usecase.GetNativeLanguage
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
        wordListRepository: WordListRepository,
        sharedPreferenceSettings: SharedPreferenceSettings,
    ): WordListUseCases {
        return WordListUseCases(
            getWordListUseCase = GetWordListUseCase(
                wordListRepository
            ),
            loadWordUseCase = LoadWordUseCase(
                wordListRepository
            ),
            deleteWordUseCase = DeleteWordUseCase(
                wordListRepository
            ),
            sortWordListUseCases = SortWordListUseCase(),
            filterWordListUseCase = FilterWordListUseCase(),
            getWordsListSortedByVideoIdUseCase = GetWordsListSortedByVideoIdUseCase(
                wordListRepository
            ),
            getNativeLanguage = GetNativeLanguage(
                sharedPreferenceSettings
            ),
            getLearningLanguage = GetLearningLanguage(
                sharedPreferenceSettings
            ),
        )
    }
}