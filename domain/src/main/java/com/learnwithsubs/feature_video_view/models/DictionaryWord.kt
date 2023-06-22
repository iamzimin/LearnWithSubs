package com.learnwithsubs.feature_video_view.models

data class DictionaryWord(
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