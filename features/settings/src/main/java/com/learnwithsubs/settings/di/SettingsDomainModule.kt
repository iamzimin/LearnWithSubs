package com.learnwithsubs.settings.di

import com.learnwithsubs.settings.domain.repository.SettingsRepository
import com.learnwithsubs.settings.domain.usecase.SettingsUseCases
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SettingsDomainModule {
    @Provides
    @Singleton
    fun provideSettingsUseCases(
        settingsRepository: SettingsRepository,
    ): SettingsUseCases {
        return SettingsUseCases()
    }
}