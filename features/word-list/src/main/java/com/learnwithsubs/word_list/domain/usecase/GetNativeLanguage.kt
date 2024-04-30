package com.learnwithsubs.word_list.domain.usecase

import com.learnwithsubs.shared_preference_settings.domain.repository.SharedPreferenceSettings

class GetNativeLanguage(
    private val sharedPreferenceSettings: SharedPreferenceSettings
) {
    fun invoke(): Pair<String, String> {
        return sharedPreferenceSettings.getNativeLanguage()
    }
}