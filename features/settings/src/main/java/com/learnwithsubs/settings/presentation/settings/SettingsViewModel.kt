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
}