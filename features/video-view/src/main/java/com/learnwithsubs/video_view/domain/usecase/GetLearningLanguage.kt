package com.learnwithsubs.video_view.domain.usecase

import com.learnwithsubs.shared_preference_settings.domain.repository.SharedPreferenceSettings

class GetLearningLanguage(
    private val sharedPreferenceSettings: SharedPreferenceSettings
) {
    fun invoke(): Pair<String, String> {
        return sharedPreferenceSettings.getLearningLanguage()
    }
}