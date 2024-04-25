package com.learnwithsubs.settings.domain.usecase

import com.learnwithsubs.shared_preference_settings.domain.repository.SharedPreferenceSettings

class GetAppStyle(
    private val sharedPreferenceSettings: SharedPreferenceSettings
) {
    fun invoke(): String {
        return sharedPreferenceSettings.getAppStyle()
    }
}