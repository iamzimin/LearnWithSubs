package com.learnwithsubs.word_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WordListViewModelFactory(
    val wordListUseCases: com.learnwithsubs.word_list.domain.usecase.WordListUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WordListViewModel(
            wordListUseCases = wordListUseCases
        ) as T
    }
}