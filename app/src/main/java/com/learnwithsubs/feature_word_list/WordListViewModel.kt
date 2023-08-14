package com.learnwithsubs.feature_word_list

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.learnwithsubs.feature_word_list.models.WordTranslation
import com.learnwithsubs.feature_word_list.usecase.WordListUseCases
import kotlinx.coroutines.launch
import javax.inject.Inject

class WordListViewModel @Inject constructor(
    val wordListUseCases: WordListUseCases
) : ViewModel() {

    val wordList = MediatorLiveData<List<WordTranslation>?>()
    var editableWord: WordTranslation? = null

    private var filter: String? = null

    init {
        updateVideoList()
    }

    fun updateVideoList() {
        wordList.addSource(wordListUseCases.getWordsListSortedByVideoIdUseCase.invoke().asLiveData()) { list ->
            wordList.value = filterVideoList(wordList = ArrayList(list))
        }
    }

    private fun filterVideoList(wordList: List<WordTranslation>): List<WordTranslation> {
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