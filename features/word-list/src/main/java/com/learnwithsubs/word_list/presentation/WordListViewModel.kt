package com.learnwithsubs.word_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.learnwithsubs.word_list.domain.models.WordTranslation
import com.learnwithsubs.word_list.domain.usecase.WordListUseCases
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

    fun getNativeLanguage(): Pair<String, String> {
        return wordListUseCases.getNativeLanguage.invoke()
    }
    fun getLearningLanguage(): Pair<String, String> {
        return wordListUseCases.getLearningLanguage.invoke()
    }


    fun setFilterMode(filter: String?) {
        this.filter = filter
    }
}