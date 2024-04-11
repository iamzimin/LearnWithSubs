package com.example.yandex_dictionary_api.models

data class DictionaryWordDTO(
    val translation: String,
    val dictionaryElement: ArrayList<DictionaryElementDTO>,
)

sealed class DictionaryElementDTO {
    data class PartSpeech(val partSpeech: String?) : DictionaryElementDTO()
    data class Synonyms(val dictionarySynonymsDTO: DictionarySynonymsDTO) : DictionaryElementDTO()
}

data class DictionarySynonymsDTO(
    val id: Int,
    val word: String,
    val translation: String,
)
