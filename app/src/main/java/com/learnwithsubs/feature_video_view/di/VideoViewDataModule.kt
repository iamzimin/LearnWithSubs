package com.learnwithsubs.feature_video_view.di

import android.content.Context
import androidx.room.Room
import com.learnwithsubs.feature_video_list.storage.VideoDatabase
import com.learnwithsubs.feature_video_view.repository.YandexDictionaryRepositoryImpl
import com.learnwithsubs.feature_video_view.repository.VideoViewRepositoryImpl
import com.learnwithsubs.feature_video_view.models.server.YandexDictionaryResponse
import com.learnwithsubs.feature_video_view.models.server.YandexTranslatorResponse
import com.learnwithsubs.feature_video_view.repository.DictionaryRepository
import com.learnwithsubs.feature_video_view.service.ServerTimeService
import com.learnwithsubs.feature_video_view.repository.ServerTimeServiceImpl
import com.learnwithsubs.feature_video_view.repository.TranslatorRepository
import com.learnwithsubs.feature_video_view.repository.VideoViewRepository
import com.learnwithsubs.feature_video_view.repository.YandexTokenRepository
import com.learnwithsubs.feature_video_view.repository.YandexTokenRepositoryImpl
import com.learnwithsubs.feature_video_view.repository.YandexTranslatorRepositoryImpl
import com.learnwithsubs.feature_video_view.storage.YandexTranslatorStorageRepository
import com.learnwithsubs.feature_video_view.storage.sharedprefs.SharedPrefsYandexTranslatorStorageRepository
import com.learnwithsubs.feature_word_list.storage.WordDatabase
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
        val BASE_URL = "https://dictionary.yandex.net/api/v1/"
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("YandexTranslator")
    fun provideYandexTranslatorRetrofit(): Retrofit {
        val BASE_URL = "https://translate.api.cloud.yandex.net/"
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("YandexIAmToken")
    fun provideYandexIAmTokenRetrofit(): Retrofit {
        val BASE_URL = "https://iam.api.cloud.yandex.net/"
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGoogleDictionaryRetrofit(): Retrofit {
        val BASE_URL = "https://????/"
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("ServerTime")
    fun provideServerTimeRetrofit(): Retrofit {
        val BASE_URL = "https://timeapi.io/api/"
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideVideoRepository(videoDB: VideoDatabase, wordDB: WordDatabase): VideoViewRepository {
        return VideoViewRepositoryImpl(videoDB.videoListDao, wordDB.wordListDao)
    }

    @Provides
    @Singleton
    fun provideSharedPrefsYandexTranslatorStorageRepository(context: Context): YandexTranslatorStorageRepository {
        return SharedPrefsYandexTranslatorStorageRepository(context)
    }

    @Provides
    @Singleton
    fun provideYandexDictionaryRepository(
        @Named("YandexDictionary") retrofit: Retrofit
    ): DictionaryRepository<YandexDictionaryResponse> {
        return YandexDictionaryRepositoryImpl(retrofit)
    }

    @Provides
    @Singleton
    fun provideYandexTokenRepository(
        @Named("YandexIAmToken") retrofitIAmToken: Retrofit,
        sharedPrefsYandexTranslator: YandexTranslatorStorageRepository
    ): YandexTokenRepository {
        return YandexTokenRepositoryImpl(retrofitIAmToken, sharedPrefsYandexTranslator)
    }

    @Provides
    @Singleton
    fun provideYandexTranslatorRepository(
        @Named("YandexTranslator") retrofitTranslator: Retrofit,
    ): TranslatorRepository<YandexTranslatorResponse> {
        return YandexTranslatorRepositoryImpl(retrofitTranslator)
    }

    @Provides
    @Singleton
    fun provideServerTimeService(
        @Named("ServerTime") serverTime: Retrofit
    ): ServerTimeService {
        return ServerTimeServiceImpl(serverTime)
    }
}