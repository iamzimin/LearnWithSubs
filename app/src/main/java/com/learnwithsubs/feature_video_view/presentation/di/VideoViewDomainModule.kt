package com.learnwithsubs.feature_video_view.presentation.di

import com.learnwithsubs.feature_video_view.domain.repository.TranslatorRepository
import com.learnwithsubs.feature_video_view.domain.repository.VideoViewRepository
import com.learnwithsubs.feature_video_view.domain.usecase.GetVideoSubtitlesUseCase
import com.learnwithsubs.feature_video_view.domain.usecase.GetWordsFromDictionaryUseCase
import com.learnwithsubs.feature_video_view.domain.usecase.UpdateVideoUseCase
import com.learnwithsubs.feature_video_view.domain.usecase.VideoViewUseCases
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class VideoViewDomainModule {

    @Provides
    @Singleton
    fun provideVideoViewUseCase(
        videoViewRepository: VideoViewRepository,
        translatorRepository: TranslatorRepository
    ): VideoViewUseCases {
        return VideoViewUseCases(
            getVideoSubtitlesUseCase = GetVideoSubtitlesUseCase(videoViewRepository),
            updateVideoUseCase = UpdateVideoUseCase(videoViewRepository),
            getWordsFromDictionaryUseCase = GetWordsFromDictionaryUseCase(translatorRepository)
        )
    }
}