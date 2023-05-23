package com.learnwithsubs.feature_video_list.domain.usecase

data class VideoUseCases(
    val getVideoListUseCase: GetVideoListUseCase,
    val deleteVideoUseCase: DeleteVideoUseCase,
    val loadVideoUseCase: LoadVideoUseCase
)
