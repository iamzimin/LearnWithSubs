package com.learnwithsubs.settings.domain.usecase

import com.learnwithsubs.shared_preference_settings.domain.repository.SharedPreferenceSettings

class GetTranslatorSource(
    private val sharedPreferenceSettings: SharedPreferenceSettings
) {
    fun invoke(): String? {
        return sharedPreferenceSettings.getTranslatorSource()
    }
}