package com.learnwithsubs.feature_word_list.model

import com.learnwithsubs.feature_word_list.models.WordTranslation

class WordManager {
    private var list = ArrayList<WordTitle>()
    private var wordList = ArrayList<WordTranslation>()

    fun setData(list: List<WordTitle>) {
        this.list.clear()
        this.wordList.clear()
        list.forEach { word ->
            this.list.add(WordTitle(word.id, word.title, ArrayList(word.wordList)))
            wordList.addAll(ArrayList(word.wordList))
        }
    }

    fun addTitle(elem: WordTitle) {
        wordList.addAll(elem.wordList)
        list.add(elem)
    }
    fun addWord(elem: WordTranslation, index: Int) {
        wordList.add(elem)
        list[index].wordList.add(elem)
    }

    fun removeTitle(index: Int) {
        wordList.clear()
        list[index].wordList.clear()
        for (word in list)
            wordList.addAll(ArrayList(word.wordList))
    }
    fun removeWord(elem: WordTranslation, index: Int) {
        wordList.removeIf { it.id == elem.id }
        list[index].wordList.removeIf { it.id == elem.id }
    }

    fun updateTitle(elem: WordTitle, index: Int) {
        list[index] = WordTitle(id = elem.id, title = elem.title, wordList = ArrayList(elem.wordList))
        wordList.clear()
        for (word in list)
            wordList.addAll(ArrayList(word.wordList))
    }

    fun clearWords() {
        list.forEach {
            it.wordList.clear()
        }
        wordList.clear()
    }

    fun getList(): List<WordTitle> = ArrayList(list)
    fun getWordsList(): List<WordTranslation> = ArrayList(wordList)
    fun getTitle(index: Int): WordTitle = list[index]
    fun getTitleSize(index: Int): Int = list[index].wordList.size
    fun getListSize(): Int = list.size
    fun getWordsListSize(): Int = wordList.size
}