package com.learnwithsubs.video_view.di

import android.content.Context
import androidx.room.Room
import com.example.yandex_translator_api.data.repository.YandexTranslatorRepositoryImpl
import com.example.yandex_translator_api.domain.repository.YandexTranslatorRepository
import com.learnwithsubs.android_translator.data.repository.AndroidTranslatorRepositoryImpl
import com.learnwithsubs.android_translator.domain.repository.AndroidTranslatorRepository
import com.learnwithsubs.database.data.storage.VideoDatabase
import com.learnwithsubs.database.data.storage.WordDatabase
import com.learnwithsubs.server_translator_api.data.repository.ServerTranslatorRepositoryImpl
import com.learnwithsubs.server_translator_api.domain.repository.ServerTranslatorRepository
import com.learnwithsubs.shared_preference_settings.data.repository.SharedPreferenceSettingsImpl
import com.learnwithsubs.shared_preference_settings.domain.repository.SharedPreferenceSettings
import com.learnwithsubs.video_view.data.repository.VideoViewRepositoryImpl
import com.learnwithsubs.video_view.domain.repository.VideoViewRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class VideoViewDataModule {

    @Provides
    @Singleton
    fun provideVideoDatabase(context: Context) : VideoDatabase {
        return Room.databaseBuilder(
            context,
            VideoDatabase::class.java,
            VideoDatabase.DATABASE_NAME
        ).build()
    }
    @Provides
    @Singleton
    fun provideWordDatabase(context: Context) : WordDatabase {
        return Room.databaseBuilder(
            context,
            WordDatabase::class.java,
            WordDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    @Named("YandexDictionary")
    fun provideYandexDictionaryRetrofit(): Retrofit {
        val baseUrl = "https://dictionary.yandex.net/api/v1/dicservice.json/"

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("Server")
    fun provideServerRetrofit(): Retrofit {
        val baseUrl = "http://192.168.0.104:8000/"

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideVideoRepository(
        videoDB: VideoDatabase,
        wordDB:WordDatabase
    ): VideoViewRepository {
        return VideoViewRepositoryImpl(
            videoDB.videoListDao,
            wordDB.wordListDao
        )
    }

    @Provides
    @Singleton
    fun provideYandexDictionaryRepository(
        @Named("YandexDictionary") yandexRetrofit: Retrofit,
    ): YandexTranslatorRepository {
        return YandexTranslatorRepositoryImpl(
            yandexRetrofit,
        )
    }

    @Provides
    @Singleton
    fun provideServerTranslatorRepository(
        @Named("Server") serverRetrofit: Retrofit
    ): ServerTranslatorRepository {
        return ServerTranslatorRepositoryImpl(
            serverRetrofit,
        )
    }

    @Provides
    @Singleton
    fun provideAndroidTranslatorRepository(): AndroidTranslatorRepository {
        return AndroidTranslatorRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideSharedPreferenceSettingsRepository(
        context: Context
    ): SharedPreferenceSettings {
        return SharedPreferenceSettingsImpl(
            context
        )
    }
}