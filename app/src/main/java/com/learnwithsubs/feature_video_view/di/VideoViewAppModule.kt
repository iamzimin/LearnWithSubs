package com.learnwithsubs.feature_video_view.di

import android.content.Context
import com.learnwithsubs.feature_video_view.usecase.VideoViewUseCases
import com.learnwithsubs.feature_video_view.videos.VideoViewViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class VideoViewAppModule(val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideVideoListViewModelFactory(
        videoViewUseCases: VideoViewUseCases
    ): VideoViewViewModelFactory {
        return VideoViewViewModelFactory(videoViewUseCases = videoViewUseCases)
    }
}