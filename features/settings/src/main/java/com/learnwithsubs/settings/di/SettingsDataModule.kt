package com.learnwithsubs.settings.di

import android.content.Context
import com.learnwithsubs.settings.data.repository.SettingsRepositoryImpl
import com.learnwithsubs.settings.domain.repository.SettingsRepository
import com.learnwithsubs.shared_preference_settings.data.repository.SharedPreferenceSettingsImpl
import com.learnwithsubs.shared_preference_settings.domain.repository.SharedPreferenceSettings
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SettingsDataModule {

    @Provides
    @Singleton
    fun provideSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideSharedPreferenceSettings(context: Context): SharedPreferenceSettings {
        return SharedPreferenceSettingsImpl(context = context)
    }
}