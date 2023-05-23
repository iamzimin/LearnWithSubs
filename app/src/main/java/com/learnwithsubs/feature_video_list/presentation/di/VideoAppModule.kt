package com.learnwithsubs.feature_video_list.presentation.di

import android.content.Context
import com.learnwithsubs.feature_video_list.domain.usecase.VideoUseCases
import com.learnwithsubs.feature_video_list.presentation.videos.VideoListViewModelFactory
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