package com.learnwithsubs.video_view.di

import com.learnwithsubs.video_view.domain.repository.TranslatorRepository
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
        videoViewRepository: com.learnwithsubs.video_view.domain.repository.VideoViewRepository,
        translatorRepository: com.learnwithsubs.video_view.domain.repository.TranslatorRepository,
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