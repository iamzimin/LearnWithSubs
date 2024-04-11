package com.example.yandex_dictionary_api.models

data class DictionaryWordDTO(
    var translation: String,
    var synonyms: ArrayList<DictionarySynonymsDTO>
)

data class DictionarySynonymsDTO(
    val id: Int,
    val word: String,
    val translation: String,
    var partSpeech: String,
    val type: DictionaryTypeDTO
)

enum class DictionaryTypeDTO(val value: Int) {
    WORD(1),
    PART_SPEECH(2)
}