package com.learnwithsubs.feature_video_list.presentation.di

import com.learnwithsubs.feature_video_list.domain.repository.VideoListRepository
import com.learnwithsubs.feature_video_list.domain.usecase.DeleteVideoUseCase
import com.learnwithsubs.feature_video_list.domain.usecase.GetVideoListUseCase
import com.learnwithsubs.feature_video_list.domain.usecase.LoadVideoUseCase
import com.learnwithsubs.feature_video_list.domain.usecase.VideoListUseCases
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class VideoListDomainModule {

    @Provides
    @Singleton
    fun provideVideoListUseCases(repository: VideoListRepository): VideoListUseCases {
        return VideoListUseCases(
            getVideoListUseCase = GetVideoListUseCase(repository),
            deleteVideoUseCase = DeleteVideoUseCase(repository),
            loadVideoUseCase = LoadVideoUseCase(repository)
        )
    }

}