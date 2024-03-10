package com.learnwithsubs.video_view.di

import android.content.Context
import com.learnwithsubs.video_view.presentation.videos.VideoViewViewModelFactory
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
        videoViewUseCases: com.learnwithsubs.video_view.domain.usecase.VideoViewUseCases
    ): VideoViewViewModelFactory {
        return VideoViewViewModelFactory(videoViewUseCases = videoViewUseCases)
    }
}