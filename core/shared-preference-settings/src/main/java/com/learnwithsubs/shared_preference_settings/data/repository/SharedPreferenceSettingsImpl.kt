package com.learnwithsubs.shared_preference_settings.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.learnwithsubs.resource.R
import com.learnwithsubs.shared_preference_settings.domain.languageIdToString
import com.learnwithsubs.shared_preference_settings.domain.repository.SharedPreferenceSettings
import com.learnwithsubs.shared_preference_settings.domain.stringToLanguageId
import com.learnwithsubs.shared_preference_settings.domain.stringToStyleId
import com.learnwithsubs.shared_preference_settings.domain.stringToTranslatorSourceId
import com.learnwithsubs.shared_preference_settings.domain.styleIdToString
import com.learnwithsubs.shared_preference_settings.domain.translatorSourceIdToString
import java.util.Locale

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

    override fun getAllAppLanguages(): Array<String> {
        return arrayOf(
            context.getString(R.string.chinese),
            context.getString(R.string.english),
            context.getString(R.string.french),
            context.getString(R.string.german),
            context.getString(R.string.italian),
            context.getString(R.string.japanese),
            context.getString(R.string.korean),
            context.getString(R.string.russian),
            context.getString(R.string.spain),
        )
    }

    override fun getAllStyles(): Array<String> {
        return arrayOf(
            context.getString(R.string.dark),
            context.getString(R.string.light),
        )
    }

    override fun getAllTranslatorSource(): Array<String> {
        return arrayOf(
            context.getString(R.string.yandex_plus_android),
            context.getString(R.string.yandex_plus_server),
        )
    }

    override fun getAllTranslatorLanguages(): Array<String> {
        return arrayOf(
            context.getString(R.string.chinese),
            context.getString(R.string.english),
            context.getString(R.string.french),
            context.getString(R.string.german),
            context.getString(R.string.italian),
            context.getString(R.string.japanese),
            context.getString(R.string.korean),
            context.getString(R.string.russian),
            context.getString(R.string.spain),
        )
    }


    override fun saveAppLanguage(language: String) {
        val languageId = stringToLanguageId(language = language, context = context)
        editor.putInt(KEY_APP_LANGUAGE, languageId)
        editor.apply()

        val languages = getAllAppLanguages()
        val locale = when (language) {
            languages[0] -> Locale("zh")
            languages[1] -> Locale("en")
            languages[2] -> Locale("fr")
            languages[3] -> Locale("de")
            languages[4] -> Locale("it")
            languages[5] -> Locale("ja")
            languages[6] -> Locale("ko")
            languages[7] -> Locale("ru")
            languages[8] -> Locale("es")
            else -> throw IllegalArgumentException("Unknown language: $language")
        }
        Locale.setDefault(locale)
        val configuration = Configuration().apply {
            setLocale(locale)
        }
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }
    override fun getAppLanguage(): Pair<String, String> {
        val languageId = sharedPreferences.getInt(KEY_APP_LANGUAGE, 2)
        return languageIdToString(id = languageId, context = context)
    }


    override fun saveAppStyle(appStyle: String) {
        val styleId = stringToStyleId(style = appStyle, context = context)
        editor.putInt(KEY_APP_STYLE, styleId)
        editor.apply()

        val styles = getAllStyles()
        val themeId = when (appStyle) {
            styles[0] -> R.style.Theme_LearnWithSubsDark
            styles[1] -> R.style.Theme_LearnWithSubsLight
            else -> throw IllegalArgumentException("Unknown app style: $appStyle")
        }
        context.setTheme(themeId)
    }
    override fun getAppStyle(): String {
        val styleId = sharedPreferences.getInt(KEY_APP_STYLE, 1)
        return styleIdToString(id = styleId, context = context)
    }

    override fun saveTranslatorSource(source: String) {
        val languageId = stringToTranslatorSourceId(translatorSource = source, context = context)
        editor.putInt(KEY_TRANSLATOR_SOURCE, languageId)
        editor.apply()
    }
    override fun getTranslatorSource(): String {
        val languageId = sharedPreferences.getInt(KEY_TRANSLATOR_SOURCE, 1)
        return translatorSourceIdToString(id = languageId, context = context)
    }

    override fun saveNativeLanguage(nativeLanguage: String) {
        val languageId = stringToLanguageId(language = nativeLanguage, context = context)
        editor.putInt(KEY_NATIVE_LANGUAGE, languageId)
        editor.apply()
    }
    override fun getNativeLanguage(): Pair<String, String> {
        val languageId = sharedPreferences.getInt(KEY_NATIVE_LANGUAGE, 8)
        return languageIdToString(id = languageId, context = context)
    }

    override fun saveLearningLanguage(learningLanguage: String) {
        val languageId = stringToLanguageId(language = learningLanguage, context = context)
        editor.putInt(KEY_LEARNING_LANGUAGE, languageId)
        editor.apply()
    }
    override fun getLearningLanguage(): Pair<String, String> {
        val languageId = sharedPreferences.getInt(KEY_LEARNING_LANGUAGE, 2)
        return languageIdToString(id = languageId, context = context)
    }
}