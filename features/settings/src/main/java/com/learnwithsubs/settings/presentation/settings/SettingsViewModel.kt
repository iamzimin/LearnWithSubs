package com.learnwithsubs.settings.presentation.settings

import androidx.lifecycle.ViewModel
import com.learnwithsubs.settings.domain.usecase.SettingsUseCases
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    val settingsUseCases: SettingsUseCases,
) : ViewModel() {

}