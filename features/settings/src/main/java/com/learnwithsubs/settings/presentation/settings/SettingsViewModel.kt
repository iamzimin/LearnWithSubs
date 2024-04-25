package com.learnwithsubs.settings.presentation.settings

import androidx.lifecycle.ViewModel
import com.learnwithsubs.settings.domain.usecase.SettingsUseCases
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    val settingsUseCases: SettingsUseCases,
) : ViewModel() {

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
    fun getAppLanguage(): String {
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
    fun getNativeLanguage(): String {
        return settingsUseCases.getNativeLanguage.invoke()
    }

    fun saveLearningLanguage(learningLanguage: String) {
        settingsUseCases.saveLearningLanguage.invoke(learningLanguage = learningLanguage)
    }
    fun getLearningLanguage(): String {
        return settingsUseCases.getLearningLanguage.invoke()
    }
}