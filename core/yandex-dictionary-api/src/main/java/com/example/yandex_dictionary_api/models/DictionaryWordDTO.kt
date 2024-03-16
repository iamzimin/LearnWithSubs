package com.example.yandex_dictionary_api.models

data class DictionaryWordDTO(
    var translation: String,
    var synonyms: ArrayList<DictionarySynonyms>
)

data class DictionarySynonyms(
    val id: Int,
    val word: String,
    val translation: String,
    var partSpeech: String,
    val type: DictionaryType
)

enum class DictionaryType(val value: Int) {
    WORD(1),
    PART_SPEECH(2)
}