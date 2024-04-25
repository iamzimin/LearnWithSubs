package com.learnwithsubs.shared_preference_settings.domain.repository

interface SharedPreferenceSettings {
    fun getAllAppLanguages(): Array<String>
    fun getAllStyles(): Array<String>
    fun getAllTranslatorSource(): Array<String>
    fun getAllTranslatorLanguages(): Array<String>

    fun saveAppLanguage(language: String)
    fun getAppLanguage(): Pair<String, String>

    fun saveAppStyle(appStyle: String)
    fun getAppStyle(): String

    fun saveTranslatorSource(source: String)
    fun getTranslatorSource(): String

    fun saveNativeLanguage(nativeLanguage: String)
    fun getNativeLanguage(): Pair<String, String>

    fun saveLearningLanguage(learningLanguage: String)
    fun getLearningLanguage(): Pair<String, String>
}