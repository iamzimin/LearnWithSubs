package com.learnwithsubs.feature_word_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.learnwithsubs.feature_word_list.models.WordTranslation
import com.learnwithsubs.feature_word_list.usecase.WordListUseCases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class WordListViewModel @Inject constructor(
    val wordListUseCases: WordListUseCases
) : ViewModel() {

    //private val wordListFlow: Flow<List<WordTranslation>> = wordListUseCases.getWordsListSortedByVideoIdUseCase.invoke()
    val wordList =  wordListUseCases.getWordsListSortedByVideoIdUseCase.invoke().asLiveData(viewModelScope.coroutineContext)

    var editableWord: WordTranslation? = null
    private var filter: String? = null

    fun filterVideoList(wordList: List<WordTranslation>): List<WordTranslation> {
        return wordListUseCases.filterWordListUseCase.invoke(wordList = ArrayList(wordList), filter = filter)
    }

    fun deleteWords(selectedWords: List<WordTranslation>?) {
        viewModelScope.launch {
            selectedWords?.toList()?.forEach {
                wordListUseCases.deleteWordUseCase.invoke(it)
            }
        }
    }

    fun editWord(word: WordTranslation) {
        viewModelScope.launch {
            wordListUseCases.loadWordUseCase.invoke(word)
        }
    }

    fun setFilterMode(filter: String?) {
        this.filter = filter
    }
}