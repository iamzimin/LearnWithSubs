package com.learnwithsubs.video_list.di

import com.learnwithsubs.database.domain.VideoListRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class VideoListDomainModule {

    @Provides
    @Singleton
    fun provideVideoListUseCases(
        videoListRepository: VideoListRepository,
        transcodeRepository: VideoTranscodeRepository,
        serverInteractionRepository: ServerInteractionRepository
    ): VideoListUseCases {
        return VideoListUseCases(
            getVideoListUseCase = GetVideoListUseCase(
                videoListRepository
            ),
            deleteVideoUseCase = DeleteVideoUseCase(
                videoListRepository
            ),
            loadVideoUseCase = LoadVideoUseCase(
                videoListRepository
            ),
            getLastVideoUseCase = GetLastVideoUseCase(
                videoListRepository
            ),
            transcodeVideoUseCase = TranscodeVideoUseCase(
                transcodeRepository
            ),
            extractAudioUseCase = ExtractAudioUseCase(
                transcodeRepository
            ),
            getSubtitlesFromServerUseCase = GetSubtitlesFromServerUseCase(
                serverInteractionRepository,
                videoListRepository
            ),
            extractVideoPreviewUseCase = ExtractVideoPreviewUseCase(
                transcodeRepository
            ),
            sortVideoListUseCase = SortVideoListUseCase(),
            loadNewSubtitlesUseCase = LoadNewSubtitlesUseCase(
                videoListRepository
            ),
            backOldSubtitlesUseCase = BackOldSubtitlesUseCase(
                videoListRepository
            ),
        )
    }

}