package com.learnwithsubs.feature_word_list.di

import com.learnwithsubs.feature_word_list.WordListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        WordListAppModule::class,
        WordListDomainModule::class,
        WordListDataModule::class,
    ]
)
interface WordListAppComponent {
    fun inject(wordListFragment: WordListFragment)
}