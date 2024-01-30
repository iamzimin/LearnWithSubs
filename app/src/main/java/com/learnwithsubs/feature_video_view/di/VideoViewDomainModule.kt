package com.learnwithsubs.feature_video_view.di

import com.learnwithsubs.feature_video_view.repository.TranslatorRepository
import com.learnwithsubs.feature_video_view.repository.VideoViewRepository
import com.learnwithsubs.feature_video_view.usecase.GetTranslationFromAndroidUseCase
import com.learnwithsubs.feature_video_view.usecase.GetTranslationFromServerUseCase
import com.learnwithsubs.feature_video_view.usecase.GetVideoSubtitlesUseCase
import com.learnwithsubs.feature_video_view.usecase.GetWordsFromYandexDictionaryUseCase
import com.learnwithsubs.feature_video_view.usecase.SaveWordUseCase
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
        translatorRepository: TranslatorRepository,
    ): VideoViewUseCases {
        return VideoViewUseCases(
            getVideoSubtitlesUseCase = GetVideoSubtitlesUseCase(videoViewRepository),
            updateVideoUseCase = UpdateVideoUseCase(videoViewRepository),
            getWordsFromYandexDictionaryUseCase = GetWordsFromYandexDictionaryUseCase(translatorRepository),
            getTranslationFromServerUseCase = GetTranslationFromServerUseCase(translatorRepository),
            getTranslationFromAndroidUseCase = GetTranslationFromAndroidUseCase(translatorRepository),
            saveWordUseCase = SaveWordUseCase(videoViewRepository)
        )
    }
}