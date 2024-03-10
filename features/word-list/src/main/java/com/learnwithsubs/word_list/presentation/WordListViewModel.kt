package com.learnwithsubs.word_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class WordListViewModel @Inject constructor(
    val wordListUseCases: com.learnwithsubs.word_list.domain.usecase.WordListUseCases
) : ViewModel() {

    //private val wordListFlow: Flow<List<WordTranslation>> = wordListUseCases.getWordsListSortedByVideoIdUseCase.invoke()
    val wordList =  wordListUseCases.getWordsListSortedByVideoIdUseCase.invoke().asLiveData(viewModelScope.coroutineContext)

    var editableWord: com.learnwithsubs.word_list.domain.models.WordTranslation? = null
    private var filter: String? = null

    fun filterVideoList(wordList: List<com.learnwithsubs.word_list.domain.models.WordTranslation>): List<com.learnwithsubs.word_list.domain.models.WordTranslation> {
        return wordListUseCases.filterWordListUseCase.invoke(wordList = ArrayList(wordList), filter = filter)
    }

    fun deleteWords(selectedWords: List<com.learnwithsubs.word_list.domain.models.WordTranslation>?) {
        viewModelScope.launch {
            selectedWords?.toList()?.forEach {
                wordListUseCases.deleteWordUseCase.invoke(it)
            }
        }
    }

    fun editWord(word: com.learnwithsubs.word_list.domain.models.WordTranslation) {
        viewModelScope.launch {
            wordListUseCases.loadWordUseCase.invoke(word)
        }
    }

    fun setFilterMode(filter: String?) {
        this.filter = filter
    }
}