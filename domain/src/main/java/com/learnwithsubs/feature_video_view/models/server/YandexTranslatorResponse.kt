package com.learnwithsubs.feature_video_view.models.server


data class YandexTranslatorResponse(
    val translations: List<TranslatorTranslation>
)

data class TranslatorTranslation(
    val text: String,
    val detectedLanguageCode: String
)