package com.learnwithsubs.settings.di

import com.learnwithsubs.settings.domain.repository.SettingsRepository
import com.learnwithsubs.settings.domain.usecase.GetAppLanguage
import com.learnwithsubs.settings.domain.usecase.GetAppStyle
import com.learnwithsubs.settings.domain.usecase.GetLearningLanguage
import com.learnwithsubs.settings.domain.usecase.GetNativeLanguage
import com.learnwithsubs.settings.domain.usecase.GetTranslatorSource
import com.learnwithsubs.settings.domain.usecase.SaveAppLanguage
import com.learnwithsubs.settings.domain.usecase.SaveAppStyle
import com.learnwithsubs.settings.domain.usecase.SaveLearningLanguage
import com.learnwithsubs.settings.domain.usecase.SaveNativeLanguage
import com.learnwithsubs.settings.domain.usecase.SaveTranslatorSource
import com.learnwithsubs.settings.domain.usecase.SettingsUseCases
import com.learnwithsubs.shared_preference_settings.domain.repository.SharedPreferenceSettings
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SettingsDomainModule {
    @Provides
    @Singleton
    fun provideSettingsUseCases(
        settingsRepository: SettingsRepository,
        sharedPreferenceSettings: SharedPreferenceSettings,
    ): SettingsUseCases {
        return SettingsUseCases(
            saveAppLanguage = SaveAppLanguage(sharedPreferenceSettings = sharedPreferenceSettings),
            getAppLanguage = GetAppLanguage(sharedPreferenceSettings = sharedPreferenceSettings),

            saveAppStyle = SaveAppStyle(sharedPreferenceSettings = sharedPreferenceSettings),
            getAppStyle = GetAppStyle(sharedPreferenceSettings = sharedPreferenceSettings),

            saveTranslatorSource = SaveTranslatorSource(sharedPreferenceSettings = sharedPreferenceSettings),
            getTranslatorSource = GetTranslatorSource(sharedPreferenceSettings = sharedPreferenceSettings),

            saveNativeLanguage = SaveNativeLanguage(sharedPreferenceSettings = sharedPreferenceSettings),
            getNativeLanguage = GetNativeLanguage(sharedPreferenceSettings = sharedPreferenceSettings),

            saveLearningLanguage = SaveLearningLanguage(sharedPreferenceSettings = sharedPreferenceSettings),
            getLearningLanguage = GetLearningLanguage(sharedPreferenceSettings = sharedPreferenceSettings),
        )
    }
}