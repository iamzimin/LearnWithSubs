package com.learnwithsubs.feature_video_view.di

import com.learnwithsubs.feature_video_view.models.server.YandexDictionaryResponse
import com.learnwithsubs.feature_video_view.models.server.YandexTranslatorResponse
import com.learnwithsubs.feature_video_view.repository.DictionaryRepository
import com.learnwithsubs.feature_video_view.repository.VideoViewRepository
import com.learnwithsubs.feature_video_view.repository.TranslatorRepository
import com.learnwithsubs.feature_video_view.repository.YandexTokenRepository
import com.learnwithsubs.feature_video_view.service.ServerTimeService
import com.learnwithsubs.feature_video_view.usecase.GetYandexTranslationUseCase
import com.learnwithsubs.feature_video_view.usecase.GetVideoSubtitlesUseCase
import com.learnwithsubs.feature_video_view.usecase.GetWordsFromYandexDictionaryUseCase
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
        yandexDictionaryRepository: DictionaryRepository<YandexDictionaryResponse>,
        yandexTranslatorRepository: TranslatorRepository<YandexTranslatorResponse>,
        yandexTokenRepository: YandexTokenRepository,
        serverTimeService: ServerTimeService,
    ): VideoViewUseCases {
        return VideoViewUseCases(
            getVideoSubtitlesUseCase = GetVideoSubtitlesUseCase(videoViewRepository),
            updateVideoUseCase = UpdateVideoUseCase(videoViewRepository),
            getWordsFromYandexDictionaryUseCase = GetWordsFromYandexDictionaryUseCase(yandexDictionaryRepository),
            getYandexTranslationUseCase = GetYandexTranslationUseCase(yandexTranslatorRepository, yandexTokenRepository, serverTimeService),
        )
    }
}