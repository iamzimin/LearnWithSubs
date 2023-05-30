package com.learnwithsubs.feature_video_view.presentation.di

import android.content.Context
import androidx.room.Room
import com.learnwithsubs.feature_video_list.data.storage.VideoDatabase
import com.learnwithsubs.feature_video_view.data.repository.VideoViewRepositoryImpl
import com.learnwithsubs.feature_video_view.domain.repository.VideoViewRepository
import dagger.Module
import dagger.Provides
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
    fun provideVideoRepository(db: VideoDatabase, context: Context): VideoViewRepository {
        return VideoViewRepositoryImpl(db.videoViewDao, context)
    }
}