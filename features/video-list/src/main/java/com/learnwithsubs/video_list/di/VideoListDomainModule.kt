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
        videoListRepository: com.learnwithsubs.database.domain.VideoListRepository,
        transcodeRepository: com.learnwithsubs.video_list.domain.repository.VideoTranscodeRepository,
        serverInteractionRepository: com.learnwithsubs.video_list.domain.repository.ServerInteractionRepository
    ): com.learnwithsubs.video_list.domain.usecase.VideoListUseCases {
        return com.learnwithsubs.video_list.domain.usecase.VideoListUseCases(
            getVideoListUseCase = com.learnwithsubs.video_list.domain.usecase.GetVideoListUseCase(
                videoListRepository
            ),
            deleteVideoUseCase = com.learnwithsubs.video_list.domain.usecase.DeleteVideoUseCase(
                videoListRepository
            ),
            loadVideoUseCase = com.learnwithsubs.video_list.domain.usecase.LoadVideoUseCase(
                videoListRepository
            ),
            getLastVideoUseCase = com.learnwithsubs.video_list.domain.usecase.GetLastVideoUseCase(
                videoListRepository
            ),
            transcodeVideoUseCase = com.learnwithsubs.video_list.domain.usecase.TranscodeVideoUseCase(
                transcodeRepository
            ),
            extractAudioUseCase = com.learnwithsubs.video_list.domain.usecase.ExtractAudioUseCase(
                transcodeRepository
            ),
            getSubtitlesFromServerUseCase = com.learnwithsubs.video_list.domain.usecase.GetSubtitlesFromServerUseCase(
                serverInteractionRepository,
                videoListRepository
            ),
            extractVideoPreviewUseCase = com.learnwithsubs.video_list.domain.usecase.ExtractVideoPreviewUseCase(
                transcodeRepository
            ),
            sortVideoListUseCase = com.learnwithsubs.video_list.domain.usecase.SortVideoListUseCase(),
            loadNewSubtitlesUseCase = com.learnwithsubs.video_list.domain.usecase.LoadNewSubtitlesUseCase(
                videoListRepository
            ),
            backOldSubtitlesUseCase = com.learnwithsubs.video_list.domain.usecase.BackOldSubtitlesUseCase(
                videoListRepository
            ),
        )
    }

}