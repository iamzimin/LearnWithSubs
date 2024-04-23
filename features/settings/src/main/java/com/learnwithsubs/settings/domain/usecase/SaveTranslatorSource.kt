package com.learnwithsubs.settings.domain.usecase

import com.learnwithsubs.shared_preference_settings.domain.repository.SharedPreferenceSettings

class SaveTranslatorSource(
    private val sharedPreferenceSettings: SharedPreferenceSettings
) {
    fun invoke(source: String) {
        sharedPreferenceSettings.saveTranslatorSource(source = source)
    }
}