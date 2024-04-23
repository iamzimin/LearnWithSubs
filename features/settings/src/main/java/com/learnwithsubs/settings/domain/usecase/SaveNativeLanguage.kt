package com.learnwithsubs.settings.domain.usecase

import com.learnwithsubs.shared_preference_settings.domain.repository.SharedPreferenceSettings

class SaveNativeLanguage(
    private val sharedPreferenceSettings: SharedPreferenceSettings
) {
    fun invoke(nativeLanguage: String) {
        sharedPreferenceSettings.saveNativeLanguage(nativeLanguage = nativeLanguage)
    }
}