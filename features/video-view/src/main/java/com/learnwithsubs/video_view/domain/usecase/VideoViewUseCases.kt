package com.learnwithsubs.video_view.domain.usecase

data class VideoViewUseCases(
    val getVideoSubtitlesUseCase: GetVideoSubtitlesUseCase,
    val updateVideoUseCase: UpdateVideoUseCase,
    val getWordsFromYandexDictionaryUseCase: GetWordsFromYandexDictionaryUseCase,
    val getTranslationFromServerUseCase: GetTranslationFromServerUseCase,
    val getTranslationFromAndroidUseCase: GetTranslationFromAndroidUseCase,
    val saveWordUseCase: SaveWordUseCase,
)
