package com.learnwithsubs.settings.presentation.settings

import androidx.lifecycle.ViewModel
import com.learnwithsubs.settings.domain.usecase.SettingsUseCases
import org.intellij.lang.annotations.Language
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    val settingsUseCases: SettingsUseCases,
) : ViewModel() {

    fun saveAppLanguage(language: String) {
        settingsUseCases.saveAppLanguage.invoke(language = language)
    }
    fun getAppLanguage(): String? {
        return settingsUseCases.getAppLanguage.invoke()
    }

    fun saveAppStyle(appStyle: String) {
        settingsUseCases.saveAppStyle.invoke(appStyle = appStyle)
    }
    fun getAppStyle(): String? {
        return settingsUseCases.getAppStyle.invoke()
    }

    fun saveTranslatorSource(source: String) {
        settingsUseCases.saveTranslatorSource.invoke(source = source)
    }
    fun getTranslatorSource(): String? {
        return settingsUseCases.getTranslatorSource.invoke()
    }

    fun saveNativeLanguage(nativeLanguage: String) {
        settingsUseCases.saveNativeLanguage.invoke(nativeLanguage = nativeLanguage)
    }
    fun getNativeLanguage(): String? {
        return settingsUseCases.getNativeLanguage.invoke()
    }

    fun saveLearningLanguage(learningLanguage: String) {
        settingsUseCases.saveLearningLanguage.invoke(learningLanguage = learningLanguage)
    }
    fun getLearningLanguage(): String? {
        return settingsUseCases.getLearningLanguage.invoke()
    }
}