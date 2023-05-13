package com.learnwithsubs.feature_video.presentation.di

import android.app.Application
import androidx.room.Room
import com.learnwithsubs.feature_video.data.repository.VideoRepositoryImpl
import com.learnwithsubs.feature_video.data.storage.VideoDatabase
import com.learnwithsubs.feature_video.domain.repository.VideoRepository
import com.learnwithsubs.feature_video.domain.usecase.DeleteVideoUseCase
import com.learnwithsubs.feature_video.domain.usecase.GetVideoListUseCase
import com.learnwithsubs.feature_video.domain.usecase.VideoUseCases
import com.learnwithsubs.feature_video.presentation.VideoListViewModel
import com.learnwithsubs.feature_video.presentation.VideoListViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VideoModule {

    @Provides
    @Singleton
    fun provideVideoDatabase(app: Application) : VideoDatabase {
        return Room.databaseBuilder(
            app,
            VideoDatabase::class.java,
            VideoDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideVideoRepository(db: VideoDatabase): VideoRepository {
        return VideoRepositoryImpl(db.videoDao)
    }

    @Provides
    @Singleton
    fun provideVideoUseCases(repository: VideoRepository): VideoUseCases {
        return VideoUseCases(
            getVideoListUseCase = GetVideoListUseCase(repository),
            deleteVideoUseCase = DeleteVideoUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideVideoListViewModelFactory(
        videoUseCases: VideoUseCases
    ): VideoListViewModelFactory {
        return VideoListViewModelFactory(videoUseCases = videoUseCases)
    }
}