package com.learnwithsubs.feature_video_view.presentation.di

import com.learnwithsubs.feature_video_view.domain.repository.VideoViewRepository
import com.learnwithsubs.feature_video_view.domain.usecase.GetVideoSubtitlesUseCase
import com.learnwithsubs.feature_video_view.domain.usecase.VideoViewUseCases
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class VideoViewDomainModule {

    @Provides
    @Singleton
    fun provideVideoViewUseCase(repository: VideoViewRepository): VideoViewUseCases {
        return VideoViewUseCases(
            getVideoSubtitlesUseCase = GetVideoSubtitlesUseCase(repository),
        )
    }
}