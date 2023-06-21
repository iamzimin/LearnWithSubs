package com.learnwithsubs.feature_video_view.models


data class TranslatorYandexResponse(
    val translations: List<TranslatorTranslation>
)

data class TranslatorTranslation(
    val text: String,
    val detectedLanguageCode: String
)