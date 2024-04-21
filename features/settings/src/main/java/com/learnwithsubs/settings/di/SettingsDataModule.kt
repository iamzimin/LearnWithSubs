package com.learnwithsubs.settings.di

import com.learnwithsubs.settings.data.repository.SettingsRepositoryImpl
import com.learnwithsubs.settings.domain.repository.SettingsRepository
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
}