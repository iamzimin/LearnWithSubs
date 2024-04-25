package com.learnwithsubs.settings.domain.usecase

import com.learnwithsubs.shared_preference_settings.domain.repository.SharedPreferenceSettings

class GetAppLanguage(
    private val sharedPreferenceSettings: SharedPreferenceSettings
) {
    fun invoke(): Pair<String, String> {
        return sharedPreferenceSettings.getAppLanguage()
    }
}