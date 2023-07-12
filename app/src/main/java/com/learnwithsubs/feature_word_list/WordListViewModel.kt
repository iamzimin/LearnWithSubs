package com.learnwithsubs.feature_word_list

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_word_list.models.WordTranslation
import com.learnwithsubs.feature_word_list.usecase.WordListUseCases
import kotlinx.coroutines.launch
import javax.inject.Inject

class WordListViewModel @Inject constructor(
    val wordListUseCases: WordListUseCases
) : ViewModel() {
    val wordList = MediatorLiveData<List<WordTranslation>?>()

    init {
        updateVideoList()
    }

    fun updateVideoList() {
        wordList.addSource(wordListUseCases.getWordListUseCase.invoke().asLiveData()) { list ->
            //wordList.value = getSortedVideoList(videoList = ArrayList(list))
            wordList.value = ArrayList(list)
        }
    }

    fun deleteWords(selectedWords: List<WordTranslation>?) {
        viewModelScope.launch {
            selectedWords?.forEach {
                wordListUseCases.deleteWordUseCase.invoke(it)
            }
        }
    }
}