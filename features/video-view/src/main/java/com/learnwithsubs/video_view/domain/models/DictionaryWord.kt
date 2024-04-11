package com.learnwithsubs.video_view.domain.models

data class DictionaryWord(
    val translation: String,
    val dictionaryElement: ArrayList<DictionaryElement>,
)

sealed class DictionaryElement {
    data class PartSpeech(val partSpeech: String?) : DictionaryElement()
    data class Synonyms(val dictionarySynonyms: DictionarySynonyms) : DictionaryElement()
}

data class DictionarySynonyms(
    val id: Int,
    val word: String,
    val translation: String,
)
