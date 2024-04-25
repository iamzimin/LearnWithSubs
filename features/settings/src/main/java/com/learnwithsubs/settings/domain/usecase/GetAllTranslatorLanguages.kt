package com.learnwithsubs.settings.domain.usecase

import com.learnwithsubs.shared_preference_settings.domain.repository.SharedPreferenceSettings

class GetAllTranslatorLanguages(
    private val sharedPreferenceSettings: SharedPreferenceSettings
) {
    fun invoke(): Array<String> {
        return sharedPreferenceSettings.getAllTranslatorLanguages()
    }
}