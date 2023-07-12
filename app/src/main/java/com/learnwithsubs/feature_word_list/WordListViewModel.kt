package com.learnwithsubs.feature_word_list

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.util.VideoOrder
import com.learnwithsubs.feature_video_list.videos.VideoListViewModel
import com.learnwithsubs.feature_word_list.models.WordTranslation
import com.learnwithsubs.feature_word_list.usecase.WordListUseCases
import com.learnwithsubs.feature_word_list.util.WordOrder
import com.learnwithsubs.general.util.OrderType
import kotlinx.coroutines.launch
import javax.inject.Inject

class WordListViewModel @Inject constructor(
    val wordListUseCases: WordListUseCases
) : ViewModel() {
    companion object {
        val DEFAULT_SORT_MODE: WordOrder = WordOrder.Date(OrderType.Descending)
    }

    val wordList = MediatorLiveData<List<WordTranslation>?>()
    var wordOrder: MutableLiveData<WordOrder> = MutableLiveData<WordOrder>().apply { value = DEFAULT_SORT_MODE }
    var editableWord: WordTranslation? = null

    private var filter: String? = null

    init {
        updateVideoList()
    }

    fun updateVideoList() {
        wordList.addSource(wordListUseCases.getWordListUseCase.invoke().asLiveData()) { list ->
            wordList.value = getSortedVideoList(wordList = ArrayList(list))
        }
    }

    fun getSortedVideoList(wordList: List<WordTranslation>): List<WordTranslation> {
        val sort = getVideoOrder()
        return wordListUseCases.sortWordListUseCases.invoke(wordList = ArrayList(wordList), sortMode = sort, filter = filter)
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

    fun setVideoOrder(orderMode: WordOrder) {
        wordOrder.value = orderMode
    }
    fun getVideoOrder(): WordOrder {
        return wordOrder.value ?: DEFAULT_SORT_MODE
    }

    fun setOrderType(newOrderType: OrderType) {
        wordOrder.value?.apply { orderType = newOrderType } ?: DEFAULT_SORT_MODE
    }
    fun getOrderType(): OrderType {
        return wordOrder.value?.orderType ?: DEFAULT_SORT_MODE.orderType
    }

    fun setFilterMode(filter: String?) {
        this.filter = filter
    }
}