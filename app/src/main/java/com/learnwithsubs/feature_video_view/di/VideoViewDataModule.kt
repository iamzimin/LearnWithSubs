package com.learnwithsubs.feature_video_view.di

import android.content.Context
import androidx.room.Room
import com.learnwithsubs.feature_video_list.storage.VideoDatabase
import com.learnwithsubs.feature_video_view.repository.YandexDictionaryRepositoryImpl
import com.learnwithsubs.feature_video_view.repository.VideoViewRepositoryImpl
import com.learnwithsubs.feature_video_view.models.server.DictionaryYandexResponse
import com.learnwithsubs.feature_video_view.models.server.YandexTranslatorResponse
import com.learnwithsubs.feature_video_view.repository.DictionaryRepository
import com.learnwithsubs.feature_video_view.repository.ServerTimeService
import com.learnwithsubs.feature_video_view.repository.ServerTimeServiceImpl
import com.learnwithsubs.feature_video_view.repository.TranslatorRepository
import com.learnwithsubs.feature_video_view.repository.VideoViewRepository
import com.learnwithsubs.feature_video_view.repository.YandexTranslatorRepositoryImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
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
    @Named("YandexDictionary")
    fun provideYandexDictionaryRetrofit(): Retrofit {
        /*
        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()
        val okHttpClient: OkHttpClient = OkHttpClient.Builder() // TODO
            .connectTimeout(1000, TimeUnit.SECONDS)
            .readTimeout(1000, TimeUnit.SECONDS).build()
         */
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
    fun provideVideoRepository(db: VideoDatabase, context: Context): VideoViewRepository {
        return VideoViewRepositoryImpl(db.videoViewDao, context)
    }

    @Provides
    @Singleton
    fun provideYandexDictionaryRepository(
        @Named("YandexDictionary") retrofit: Retrofit
    ): DictionaryRepository<DictionaryYandexResponse> {
        return YandexDictionaryRepositoryImpl(retrofit)
    }

    @Provides
    @Singleton
    fun provideYandexTranslatorRepository(
        @Named("YandexTranslator") retrofitTranslator: Retrofit,
        @Named("YandexIAmToken") retrofitIAmToken: Retrofit
    ): TranslatorRepository<YandexTranslatorResponse> {
        return YandexTranslatorRepositoryImpl(retrofitTranslator, retrofitIAmToken)
    }

    @Provides
    @Singleton
    fun provideServerTimeService(
        @Named("ServerTime") serverTime: Retrofit
    ): ServerTimeService {
        return ServerTimeServiceImpl(serverTime)
    }
}