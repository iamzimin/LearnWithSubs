package com.learnwithsubs.settings.di

import android.content.Context
import com.learnwithsubs.settings.domain.usecase.SettingsUseCases
import com.learnwithsubs.settings.presentation.settings.SettingsViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SettingsAppModule(val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideSettingsViewModelFactory(
        settingsUseCases: SettingsUseCases
    ): SettingsViewModelFactory {
        return SettingsViewModelFactory(
            settingsUseCases = settingsUseCases,
        )
    }
}