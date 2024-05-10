package com.learnwithsubs.video_list.di

import android.content.Context
import androidx.room.Room
import com.example.server_api.data.repository.ServerInteractionRepositoryImpl
import com.example.server_api.domain.repository.ServerInteractionRepository
import com.example.video_transcode.data.repository.VideoTranscodeRepositoryImpl
import com.example.video_transcode.domain.repository.VideoTranscodeRepository
import com.learnwithsubs.database.data.storage.VideoDatabase
import com.learnwithsubs.video_list.data.repository.VideoListRepositoryImpl
import com.learnwithsubs.video_list.domain.repository.VideoListRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
        return Retrofit.Builder()
            .baseUrl("http://192.168.0.100:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideVideoListRepository(db: VideoDatabase): VideoListRepository {
        return VideoListRepositoryImpl(db.videoListDao)
    }

    @Provides
    @Singleton
    fun provideVideoTranscodeRepository(context: Context): VideoTranscodeRepository {
        return VideoTranscodeRepositoryImpl(
            context = context
        )
    }

    @Provides
    @Singleton
    fun provideServerInteractionRepository(retrofit: Retrofit): ServerInteractionRepository {
        return ServerInteractionRepositoryImpl(
            retrofit
        )
    }

}