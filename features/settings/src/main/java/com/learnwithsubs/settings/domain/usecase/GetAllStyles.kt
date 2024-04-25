package com.learnwithsubs.settings.domain.usecase

import com.learnwithsubs.shared_preference_settings.domain.repository.SharedPreferenceSettings

class GetAllStyles(
    private val sharedPreferenceSettings: SharedPreferenceSettings
) {
    fun invoke(): Array<String> {
        return sharedPreferenceSettings.getAllStyles()
    }
}