package com.learnwithsubs.settings.domain.usecase

import com.learnwithsubs.shared_preference_settings.domain.repository.SharedPreferenceSettings

class SaveLearningLanguage(
    private val sharedPreferenceSettings: SharedPreferenceSettings
) {
    fun invoke(learningLanguage: String) {
        sharedPreferenceSettings.saveLearningLanguage(learningLanguage = learningLanguage)
    }
}