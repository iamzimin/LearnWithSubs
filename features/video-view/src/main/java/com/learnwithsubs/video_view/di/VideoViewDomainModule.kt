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
        videoViewRepository: com.learnwithsubs.video_view.domain.repository.VideoViewRepository,
        translatorRepository: com.example.yandex_dictionary_api.domain.TranslatorRepository,
    ): com.learnwithsubs.video_view.domain.usecase.VideoViewUseCases {
        return com.learnwithsubs.video_view.domain.usecase.VideoViewUseCases(
            getVideoSubtitlesUseCase = com.learnwithsubs.video_view.domain.usecase.GetVideoSubtitlesUseCase(
                videoViewRepository
            ),
            updateVideoUseCase = com.learnwithsubs.video_view.domain.usecase.UpdateVideoUseCase(
                videoViewRepository
            ),
            getWordsFromYandexDictionaryUseCase = com.learnwithsubs.video_view.domain.usecase.GetWordsFromYandexDictionaryUseCase(
                translatorRepository
            ),
            getTranslationFromServerUseCase = com.learnwithsubs.video_view.domain.usecase.GetTranslationFromServerUseCase(
                translatorRepository
            ),
            getTranslationFromAndroidUseCase = com.learnwithsubs.video_view.domain.usecase.GetTranslationFromAndroidUseCase(
                translatorRepository
            ),
            saveWordUseCase = com.learnwithsubs.video_view.domain.usecase.SaveWordUseCase(
                videoViewRepository
            )
        )
    }
}