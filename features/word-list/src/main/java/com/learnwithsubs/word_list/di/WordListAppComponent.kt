package com.learnwithsubs.word_list.di

import com.learnwithsubs.word_list.presentation.WordListFragment
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