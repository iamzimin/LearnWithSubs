package com.learnwithsubs.feature_video_view.usecase

data class VideoViewUseCases(
    val getVideoSubtitlesUseCase: GetVideoSubtitlesUseCase,
    val updateVideoUseCase: UpdateVideoUseCase,
    val getWordsFromYandexDictionaryUseCase: GetWordsFromYandexDictionaryUseCase,
    val getTranslationFromServerUseCase: GetTranslationFromServerUseCase,
    val saveWordUseCase: SaveWordUseCase,
)
