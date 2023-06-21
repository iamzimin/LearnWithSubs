package com.learnwithsubs.feature_video_list.di

import android.content.Context
import com.learnwithsubs.feature_video_list.repository.VideoTranscodeRepository
import com.learnwithsubs.feature_video_list.usecase.VideoListUseCases
import com.learnwithsubs.feature_video_list.videos.VideoListViewModelFactory
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
        videoListUseCases: VideoListUseCases,
        videoTranscodeRepository: VideoTranscodeRepository
    ): VideoListViewModelFactory {
        return VideoListViewModelFactory(
            videoListUseCases = videoListUseCases,
            videoTranscodeRepository = videoTranscodeRepository
        )
    }
}