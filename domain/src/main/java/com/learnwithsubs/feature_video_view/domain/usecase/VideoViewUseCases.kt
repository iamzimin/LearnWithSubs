package com.learnwithsubs.feature_video_view.domain.usecase

data class VideoViewUseCases(
    val getVideoSubtitlesUseCase: GetVideoSubtitlesUseCase,
    val updateVideoUseCase: UpdateVideoUseCase,
    val getWordsFromDictionaryUseCase: GetWordsFromDictionaryUseCase
)
