package com.learnwithsubs.feature_video_view.models

data class YandexTranslatorBody(
    val targetLanguageCode: String,
    val texts: String,
    val folderId: String
)