package com.learnwithsubs.settings.presentation.settings

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.learnwithsubs.settings.domain.usecase.SettingsUseCases
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    val settingsUseCases: SettingsUseCases,
) : ViewModel() {
    var nativeDownloadingTask: Task<Void>? = null
    var learningDownloadingTask: Task<Void>? = null

    var nativeLanguagePair: Pair<String, String> = getNativeLanguage()
    var learningLanguagePair: Pair<String, String> = getLearningLanguage()

    fun updateLanguagePair() {
        nativeLanguagePair = getNativeLanguage()
        learningLanguagePair = getLearningLanguage()
    }

    fun getAllAppLanguages(): Array<String> {
        return settingsUseCases.getAllAppLanguages.invoke()
    }
    fun getAllStyles(): Array<String> {
        return settingsUseCases.getAllStyles.invoke()
    }
    fun getAllTranslatorLanguages(): Array<String> {
        return settingsUseCases.getAllTranslatorLanguages.invoke()
    }
    fun getAllTranslatorSource(): Array<String> {
        return settingsUseCases.getAllTranslatorSource.invoke()
    }

    fun saveAppLanguage(language: String) {
        settingsUseCases.saveAppLanguage.invoke(language = language)
    }
    fun getAppLanguage(): Pair<String, String> {
        return settingsUseCases.getAppLanguage.invoke()
    }

    fun saveAppStyle(appStyle: String) {
        settingsUseCases.saveAppStyle.invoke(appStyle = appStyle)
    }
    fun getAppStyle(): String {
        return settingsUseCases.getAppStyle.invoke()
    }

    fun saveTranslatorSource(source: String) {
        settingsUseCases.saveTranslatorSource.invoke(source = source)
    }
    fun getTranslatorSource(): String {
        return settingsUseCases.getTranslatorSource.invoke()
    }

    fun saveNativeLanguage(nativeLanguage: String) {
        settingsUseCases.saveNativeLanguage.invoke(nativeLanguage = nativeLanguage)
    }
    fun getNativeLanguage(): Pair<String, String> {
        return settingsUseCases.getNativeLanguage.invoke()
    }

    fun saveLearningLanguage(learningLanguage: String) {
        settingsUseCases.saveLearningLanguage.invoke(learningLanguage = learningLanguage)
    }
    fun getLearningLanguage(): Pair<String, String> {
        return settingsUseCases.getLearningLanguage.invoke()
    }
}