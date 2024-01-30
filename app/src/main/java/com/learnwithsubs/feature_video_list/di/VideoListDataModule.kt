package com.learnwithsubs.feature_video_list.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.learnwithsubs.feature_video_list.repository.ServerInteractionRepositoryImpl
import com.learnwithsubs.feature_video_list.repository.VideoListRepositoryImpl
import com.learnwithsubs.feature_video_list.repository.VideoTranscodeRepositoryImpl
import com.learnwithsubs.feature_video_list.storage.VideoDatabase
import com.learnwithsubs.feature_video_list.repository.ServerInteractionRepository
import com.learnwithsubs.feature_video_list.repository.VideoListRepository
import com.learnwithsubs.feature_video_list.repository.VideoTranscodeRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class VideoListDataModule {

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
    fun provideVideoListRepository(db: VideoDatabase): VideoListRepository {
        return VideoListRepositoryImpl(db.videoListDao)
    }

    @Provides
    @Singleton
    fun provideVideoTranscodeRepository(): VideoTranscodeRepository {
        return VideoTranscodeRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideServerInteractionRepository(retrofit: Retrofit): ServerInteractionRepository {
        return ServerInteractionRepositoryImpl(retrofit)
    }

}