package com.learnwithsubs.settings.domain.usecase

data class SettingsUseCases(
    val saveAppLanguage: SaveAppLanguage,
    val getAppLanguage: GetAppLanguage,

    val saveAppStyle: SaveAppStyle,
    val getAppStyle: GetAppStyle,

    val saveTranslatorSource: SaveTranslatorSource,
    val getTranslatorSource: GetTranslatorSource,

    val saveNativeLanguage: SaveNativeLanguage,
    val getNativeLanguage: GetNativeLanguage,

    val saveLearningLanguage: SaveLearningLanguage,
    val getLearningLanguage: GetLearningLanguage,
)