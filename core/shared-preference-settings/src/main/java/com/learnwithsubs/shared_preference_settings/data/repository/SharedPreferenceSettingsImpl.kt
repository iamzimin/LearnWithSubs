package com.learnwithsubs.shared_preference_settings.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.learnwithsubs.shared_preference_settings.R
import com.learnwithsubs.shared_preference_settings.domain.repository.SharedPreferenceSettings

class SharedPreferenceSettingsImpl(private val context: Context) : SharedPreferenceSettings {
    companion object {
        private const val PREF_FILE_NAME = "LearnWithSubs"
        private const val KEY_APP_LANGUAGE = "app_language"
        private const val KEY_APP_STYLE = "app_style"
        private const val KEY_TRANSLATOR_SOURCE = "translator_source"
        private const val KEY_NATIVE_LANGUAGE = "native_language"
        private const val KEY_LEARNING_LANGUAGE = "learning_language"
    }


    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    override fun saveAppLanguage(language: String) {
        editor.putString(KEY_APP_LANGUAGE, language)
        editor.apply()
    }

    override fun getAppLanguage(): String? {
        return sharedPreferences.getString(KEY_APP_LANGUAGE, context.getString(R.string.english))
    }

    override fun saveAppStyle(appStyle: String) {
        editor.putString(KEY_APP_STYLE, appStyle)
        editor.apply()
    }

    override fun getAppStyle(): String? {
        return sharedPreferences.getString(KEY_APP_STYLE, context.getString(R.string.dark))
    }

    override fun saveTranslatorSource(source: String) {
        editor.putString(KEY_TRANSLATOR_SOURCE, source)
        editor.apply()
    }

    override fun getTranslatorSource(): String? {
        return sharedPreferences.getString(KEY_TRANSLATOR_SOURCE, context.getString(R.string.android))
    }

    override fun saveNativeLanguage(nativeLanguage: String) {
        editor.putString(KEY_NATIVE_LANGUAGE, nativeLanguage)
        editor.apply()
    }

    override fun getNativeLanguage(): String? {
        return sharedPreferences.getString(KEY_NATIVE_LANGUAGE, context.getString(R.string.russian))
    }

    override fun saveLearningLanguage(learningLanguage: String) {
        editor.putString(KEY_LEARNING_LANGUAGE, learningLanguage)
        editor.apply()
    }

    override fun getLearningLanguage(): String? {
        return sharedPreferences.getString(KEY_LEARNING_LANGUAGE, context.getString(R.string.english))
    }
}