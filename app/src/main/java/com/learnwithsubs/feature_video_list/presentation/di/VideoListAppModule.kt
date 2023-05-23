package com.learnwithsubs.feature_video_list.presentation.di

import android.content.Context
import com.learnwithsubs.feature_video_list.domain.usecase.VideoListUseCases
import com.learnwithsubs.feature_video_list.presentation.videos.VideoListViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class VideoListAppModule(val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideVideoListViewModelFactory(
        videoListUseCases: VideoListUseCases
    ): VideoListViewModelFactory {
        return VideoListViewModelFactory(videoListUseCases = videoListUseCases)
    }
}