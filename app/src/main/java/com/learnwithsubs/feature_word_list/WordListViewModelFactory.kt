package com.learnwithsubs.feature_word_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learnwithsubs.feature_word_list.usecase.WordListUseCases

class WordListViewModelFactory(
    val wordListUseCases: WordListUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WordListViewModel(
            wordListUseCases = wordListUseCases
        ) as T
    }
}