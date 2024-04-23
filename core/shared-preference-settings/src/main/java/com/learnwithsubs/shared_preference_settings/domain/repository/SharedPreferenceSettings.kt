package com.learnwithsubs.shared_preference_settings.domain.repository

interface SharedPreferenceSettings {
    fun saveAppLanguage(language: String)
    fun getAppLanguage(): String?

    fun saveAppStyle(appStyle: String)
    fun getAppStyle(): String?

    fun saveTranslatorSource(source: String)
    fun getTranslatorSource(): String?

    fun saveNativeLanguage(nativeLanguage: String)
    fun getNativeLanguage(): String?

    fun saveLearningLanguage(learningLanguage: String)
    fun getLearningLanguage(): String?
}