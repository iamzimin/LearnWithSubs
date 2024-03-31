package com.learnwithsubs.video_view.di

import com.example.yandex_dictionary_api.domain.repository.TranslatorRepository
import com.learnwithsubs.video_view.domain.repository.VideoViewRepository
import com.learnwithsubs.video_view.domain.usecase.GetTranslationFromAndroidUseCase
import com.learnwithsubs.video_view.domain.usecase.GetTranslationFromServerUseCase
import com.learnwithsubs.video_view.domain.usecase.GetVideoSubtitlesUseCase
import com.learnwithsubs.video_view.domain.usecase.GetWordsFromYandexDictionaryUseCase
import com.learnwithsubs.video_view.domain.usecase.SaveWordUseCase
import com.learnwithsubs.video_view.domain.usecase.UpdateVideoUseCase
import com.learnwithsubs.video_view.domain.usecase.VideoViewUseCases
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
            getVideoSubtitlesUseCase = GetVideoSubtitlesUseCase(
                videoViewRepository
            ),
            updateVideoUseCase = UpdateVideoUseCase(
                videoViewRepository
            ),
            getWordsFromYandexDictionaryUseCase = GetWordsFromYandexDictionaryUseCase(
                translatorRepository
            ),
            getTranslationFromServerUseCase = GetTranslationFromServerUseCase(
                translatorRepository
            ),
            getTranslationFromAndroidUseCase = GetTranslationFromAndroidUseCase(
                translatorRepository
            ),
            saveWordUseCase = SaveWordUseCase(
                videoViewRepository
            )
        )
    }
}