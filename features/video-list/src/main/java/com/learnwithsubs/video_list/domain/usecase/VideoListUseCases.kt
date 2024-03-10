package com.learnwithsubs.video_list.domain.usecase

data class VideoListUseCases(
    val getVideoListUseCase: GetVideoListUseCase,
    val sortVideoListUseCase: SortVideoListUseCase,
    val deleteVideoUseCase: DeleteVideoUseCase,
    val loadVideoUseCase: LoadVideoUseCase,
    val getLastVideoUseCase: GetLastVideoUseCase,
    val transcodeVideoUseCase: TranscodeVideoUseCase,
    val extractAudioUseCase: ExtractAudioUseCase,
    val extractVideoPreviewUseCase: ExtractVideoPreviewUseCase,
    val getSubtitlesFromServerUseCase: GetSubtitlesFromServerUseCase,
    val loadNewSubtitlesUseCase: LoadNewSubtitlesUseCase,
    val backOldSubtitlesUseCase: BackOldSubtitlesUseCase,
)
