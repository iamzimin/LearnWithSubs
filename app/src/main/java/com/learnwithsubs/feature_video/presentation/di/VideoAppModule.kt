package com.learnwithsubs.feature_video.presentation.di

import android.content.Context
import com.learnwithsubs.feature_video.domain.usecase.VideoUseCases
import com.learnwithsubs.feature_video.presentation.videos.VideoListViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class VideoAppModule(val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideVideoListViewModelFactory(
        videoUseCases: VideoUseCases
    ): VideoListViewModelFactory {
        return VideoListViewModelFactory(videoUseCases = videoUseCases)
    }
}