package com.learnwithsubs.settings.di

import com.learnwithsubs.settings.presentation.SettingsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        SettingsAppModule::class,
        SettingsDataModule::class,
        SettingsDomainModule::class,
    ]
)
interface SettingsAppComponent {
    fun inject(settingsFragment: SettingsFragment)
}