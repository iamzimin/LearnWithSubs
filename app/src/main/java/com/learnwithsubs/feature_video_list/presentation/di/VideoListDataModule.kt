package com.learnwithsubs.feature_video_list.presentation.di

import android.content.Context
import androidx.room.Room
import com.learnwithsubs.feature_video_list.data.repository.VideoListRepositoryImpl
import com.learnwithsubs.feature_video_list.data.storage.VideoDatabase
import com.learnwithsubs.feature_video_list.domain.repository.VideoListRepository
import dagger.Module
import dagger.Provides
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
    fun provideVideoRepository(db: VideoDatabase): VideoListRepository {
        return VideoListRepositoryImpl(db.videoListDao)
    }

}