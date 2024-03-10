package com.learnwithsubs.video_list.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.learnwithsubs.video_list.data.repository.ServerInteractionRepositoryImpl
import com.learnwithsubs.video_list.data.repository.VideoListRepositoryImpl
import com.learnwithsubs.video_list.data.repository.VideoTranscodeRepositoryImpl
import com.learnwithsubs.video_list.data.storage.VideoDatabase
import com.learnwithsubs.video_list.domain.repository.ServerInteractionRepository
import com.learnwithsubs.video_list.domain.repository.VideoListRepository
import com.learnwithsubs.video_list.domain.repository.VideoTranscodeRepository
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
    fun provideVideoDatabase(context: Context) : com.learnwithsubs.video_list.data.storage.VideoDatabase {
        return Room.databaseBuilder(
            context,
            com.learnwithsubs.video_list.data.storage.VideoDatabase::class.java,
            com.learnwithsubs.video_list.data.storage.VideoDatabase.DATABASE_NAME
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
    fun provideVideoListRepository(db: com.learnwithsubs.video_list.data.storage.VideoDatabase): com.learnwithsubs.video_list.domain.repository.VideoListRepository {
        return com.learnwithsubs.video_list.data.repository.VideoListRepositoryImpl(db.videoListDao)
    }

    @Provides
    @Singleton
    fun provideVideoTranscodeRepository(): com.learnwithsubs.video_list.domain.repository.VideoTranscodeRepository {
        return com.learnwithsubs.video_list.data.repository.VideoTranscodeRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideServerInteractionRepository(retrofit: Retrofit): com.learnwithsubs.video_list.domain.repository.ServerInteractionRepository {
        return com.learnwithsubs.video_list.data.repository.ServerInteractionRepositoryImpl(retrofit)
    }

}