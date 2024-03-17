package com.learnwithsubs.video_list.di

import android.content.Context
import com.example.video_transcode.domain.repository.VideoTranscodeRepository
import com.learnwithsubs.video_list.domain.usecase.VideoListUseCases
import com.learnwithsubs.video_list.presentation.videos.VideoListViewModelFactory
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