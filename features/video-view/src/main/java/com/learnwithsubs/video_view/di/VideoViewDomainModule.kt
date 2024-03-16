package com.learnwithsubs.video_view.di

import com.example.yandex_dictionary_api.domain.TranslatorRepository
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