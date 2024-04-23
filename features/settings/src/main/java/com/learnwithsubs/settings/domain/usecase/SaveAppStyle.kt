package com.learnwithsubs.settings.domain.usecase

import com.learnwithsubs.shared_preference_settings.domain.repository.SharedPreferenceSettings

class SaveAppStyle(
    private val sharedPreferenceSettings: SharedPreferenceSettings
) {
    fun invoke(appStyle: String) {
        sharedPreferenceSettings.saveAppStyle(appStyle = appStyle)
    }
}