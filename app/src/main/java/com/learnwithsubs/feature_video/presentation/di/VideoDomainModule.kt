package com.learnwithsubs.feature_video.presentation.di

import com.learnwithsubs.feature_video.domain.repository.VideoRepository
import com.learnwithsubs.feature_video.domain.usecase.DeleteVideoUseCase
import com.learnwithsubs.feature_video.domain.usecase.GetVideoListUseCase
import com.learnwithsubs.feature_video.domain.usecase.LoadVideoUseCase
import com.learnwithsubs.feature_video.domain.usecase.VideoUseCases
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class VideoDomainModule {

    @Provides
    @Singleton
    fun provideVideoUseCases(repository: VideoRepository): VideoUseCases {
        return VideoUseCases(
            getVideoListUseCase = GetVideoListUseCase(repository),
            deleteVideoUseCase = DeleteVideoUseCase(repository),
            loadVideoUseCase = LoadVideoUseCase(repository)
        )
    }

}