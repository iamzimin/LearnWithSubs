package com.learnwithsubs.settings.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learnwithsubs.settings.domain.usecase.SettingsUseCases

class SettingsViewModelFactory(
    val settingsUseCases: SettingsUseCases,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            settingsUseCases = settingsUseCases,
        ) as T
    }
}