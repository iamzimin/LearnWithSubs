package com.learnwithsubs.feature_video_view.presentation.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.learnwithsubs.feature_video_list.data.storage.VideoDatabase
import com.learnwithsubs.feature_video_view.data.repository.TranslatorRepositoryImpl
import com.learnwithsubs.feature_video_view.data.repository.VideoViewRepositoryImpl
import com.learnwithsubs.feature_video_view.domain.repository.TranslatorRepository
import com.learnwithsubs.feature_video_view.domain.repository.VideoViewRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
    fun provideRetrofit(): Retrofit {
        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        val okHttpClient: OkHttpClient = OkHttpClient.Builder() // TODO
            .connectTimeout(1000, TimeUnit.SECONDS)
            .readTimeout(1000, TimeUnit.SECONDS).build()

        val BASE_URL = "https://dictionary.yandex.net/api/v1/"

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
    fun provideTranslatorRepository(retrofit: Retrofit): TranslatorRepository {
        return TranslatorRepositoryImpl(retrofit)
    }
}