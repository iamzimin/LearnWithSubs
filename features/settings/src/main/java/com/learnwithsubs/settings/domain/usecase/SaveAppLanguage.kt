package com.learnwithsubs.settings.domain.usecase

import com.learnwithsubs.shared_preference_settings.domain.repository.SharedPreferenceSettings

class SaveAppLanguage(
    private val sharedPreferenceSettings: SharedPreferenceSettings
) {
    fun invoke(language: String) {
        sharedPreferenceSettings.saveAppLanguage(language = language)
    }
}