package com.learnwithsubs.feature_video_view.di

import com.learnwithsubs.feature_video_view.models.DictionaryYandexResponse
import com.learnwithsubs.feature_video_view.repository.TranslatorRepository
import com.learnwithsubs.feature_video_view.repository.VideoViewRepository
import com.learnwithsubs.feature_video_view.usecase.GetVideoSubtitlesUseCase
import com.learnwithsubs.feature_video_view.usecase.GetWordsFromDictionaryUseCase
import com.learnwithsubs.feature_video_view.usecase.UpdateVideoUseCase
import com.learnwithsubs.feature_video_view.usecase.VideoViewUseCases
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class VideoViewDomainModule {

    @Provides
    @Singleton
    fun provideVideoViewUseCase(
        videoViewRepository: VideoViewRepository,
        translatorRepository: TranslatorRepository<DictionaryYandexResponse>
    ): VideoViewUseCases {
        return VideoViewUseCases(
            getVideoSubtitlesUseCase = GetVideoSubtitlesUseCase(videoViewRepository),
            updateVideoUseCase = UpdateVideoUseCase(videoViewRepository),
            getWordsFromDictionaryUseCase = GetWordsFromDictionaryUseCase(translatorRepository)
        )
    }
}