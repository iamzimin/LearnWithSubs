package com.learnwithsubs.settings.di

import com.learnwithsubs.settings.domain.usecase.GetAllAppLanguages
import com.learnwithsubs.settings.domain.usecase.GetAllStyles
import com.learnwithsubs.settings.domain.usecase.GetAllTranslatorLanguages
import com.learnwithsubs.settings.domain.usecase.GetAllTranslatorSource
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
        sharedPreferenceSettings: SharedPreferenceSettings,
    ): SettingsUseCases {
        return SettingsUseCases(
            getAllAppLanguages = GetAllAppLanguages(sharedPreferenceSettings = sharedPreferenceSettings),
            getAllStyles = GetAllStyles(sharedPreferenceSettings = sharedPreferenceSettings),
            getAllTranslatorLanguages = GetAllTranslatorLanguages(sharedPreferenceSettings = sharedPreferenceSettings),
            getAllTranslatorSource = GetAllTranslatorSource(sharedPreferenceSettings = sharedPreferenceSettings),

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