package com.learnwithsubs.feature_video_list.usecase

data class VideoListUseCases(
    val getVideoListUseCase: GetVideoListUseCase,
    val deleteVideoUseCase: DeleteVideoUseCase,
    val loadVideoUseCase: LoadVideoUseCase,
    val getLastVideoUseCase: GetLastVideoUseCase,
    val transcodeVideoUseCase: TranscodeVideoUseCase,
    val extractAudioUseCase: ExtractAudioUseCase,
    val extractVideoPreviewUseCase: ExtractVideoPreviewUseCase,
    val getSubtitlesFromServerUseCase: GetSubtitlesFromServerUseCase,
)
