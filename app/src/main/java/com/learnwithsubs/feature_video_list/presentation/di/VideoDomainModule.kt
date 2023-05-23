package com.learnwithsubs.feature_video_list.presentation.di

import com.learnwithsubs.feature_video_list.domain.repository.VideoRepository
import com.learnwithsubs.feature_video_list.domain.usecase.DeleteVideoUseCase
import com.learnwithsubs.feature_video_list.domain.usecase.GetVideoListUseCase
import com.learnwithsubs.feature_video_list.domain.usecase.LoadVideoUseCase
import com.learnwithsubs.feature_video_list.domain.usecase.VideoUseCases
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