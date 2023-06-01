package com.learnwithsubs.feature_video_list.domain.usecase

data class VideoListUseCases(
    val getVideoListUseCase: GetVideoListUseCase,
    val deleteVideoUseCase: DeleteVideoUseCase,
    val loadVideoUseCase: LoadVideoUseCase,
    val getLastVideoUseCase: GetLastVideoUseCase,
    val transcodeVideoUseCase: TranscodeVideoUseCase,
    val extractAudioUseCase: ExtractAudioUseCase,
    val sendAudioToServerUseCase: SendAudioToServerUseCase,
)
