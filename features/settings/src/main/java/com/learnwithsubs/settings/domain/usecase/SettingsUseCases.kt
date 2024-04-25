package com.learnwithsubs.settings.domain.usecase

data class SettingsUseCases(
    val getAllAppLanguages: GetAllAppLanguages,
    val getAllStyles: GetAllStyles,
    val getAllTranslatorLanguages: GetAllTranslatorLanguages,
    val getAllTranslatorSource: GetAllTranslatorSource,

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