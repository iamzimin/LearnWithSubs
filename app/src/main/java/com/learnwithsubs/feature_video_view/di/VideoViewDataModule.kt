package com.learnwithsubs.feature_video_view.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.learnwithsubs.feature_video_list.storage.VideoDatabase
import com.learnwithsubs.feature_video_view.repository.VideoViewRepositoryImpl
import com.learnwithsubs.feature_video_view.repository.TranslatorRepository
import com.learnwithsubs.feature_video_view.repository.TranslatorRepositoryImpl
import com.learnwithsubs.feature_video_view.repository.VideoViewRepository
import com.learnwithsubs.feature_word_list.storage.WordDatabase
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
    @Named("Server")
    fun provideServerRetrofit(): Retrofit {
        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS).build()

        return Retrofit.Builder()
            //.baseUrl("http://10.0.2.2:8000/")
            .baseUrl("http://192.168.0.107:8000/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideVideoRepository(videoDB: VideoDatabase, wordDB: WordDatabase): VideoViewRepository {
        return VideoViewRepositoryImpl(videoDB.videoListDao, wordDB.wordListDao)
    }

    @Provides
    @Singleton
    fun provideYandexDictionaryRepository(
        @Named("YandexDictionary") yandexRetrofit: Retrofit,
        @Named("Server") serverRetrofit: Retrofit
    ): TranslatorRepository {
        return TranslatorRepositoryImpl(yandexRetrofit, serverRetrofit)
    }
}