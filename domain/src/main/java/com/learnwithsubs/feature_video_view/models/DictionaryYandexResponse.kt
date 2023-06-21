package com.learnwithsubs.feature_video_view.models

data class DictionaryYandexResponse(
    val head: Any?,
    val def: List<Definition>
)

data class Definition(
    val text: String,
    val pos: String,
    //val ts: String,
    val tr: List<DictionaryTranslation>
)

data class DictionaryTranslation(
    val text: String,
    val pos: String,
    //val gen: String?,
    //val fr: Int?,
    val syn: List<Synonym>?,
    val mean: List<Meaning>?
)

data class Synonym(
    val text: String,
    val pos: String,
    //val gen: String?,
    //val fr: Int?
)

data class Meaning(
    val text: String
)