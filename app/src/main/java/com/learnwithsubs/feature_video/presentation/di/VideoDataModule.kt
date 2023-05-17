package com.learnwithsubs.feature_video.presentation.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.learnwithsubs.feature_video.data.repository.VideoRepositoryImpl
import com.learnwithsubs.feature_video.data.storage.VideoDatabase
import com.learnwithsubs.feature_video.domain.repository.VideoRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class VideoDataModule {

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
    fun provideVideoRepository(db: VideoDatabase): VideoRepository {
        return VideoRepositoryImpl(db.videoDao)
    }

}