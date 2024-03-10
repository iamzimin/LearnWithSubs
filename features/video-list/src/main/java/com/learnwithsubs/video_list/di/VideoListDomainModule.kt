package com.learnwithsubs.video_list.di

import com.learnwithsubs.video_list.domain.repository.ServerInteractionRepository
import com.learnwithsubs.video_list.domain.repository.VideoListRepository
import com.learnwithsubs.video_list.domain.repository.VideoTranscodeRepository
import com.learnwithsubs.video_list.domain.usecase.BackOldSubtitlesUseCase
import com.learnwithsubs.video_list.domain.usecase.DeleteVideoUseCase
import com.learnwithsubs.video_list.domain.usecase.ExtractAudioUseCase
import com.learnwithsubs.video_list.domain.usecase.ExtractVideoPreviewUseCase
import com.learnwithsubs.video_list.domain.usecase.GetLastVideoUseCase
import com.learnwithsubs.video_list.domain.usecase.GetVideoListUseCase
import com.learnwithsubs.video_list.domain.usecase.LoadVideoUseCase
import com.learnwithsubs.video_list.domain.usecase.GetSubtitlesFromServerUseCase
import com.learnwithsubs.video_list.domain.usecase.LoadNewSubtitlesUseCase
import com.learnwithsubs.video_list.domain.usecase.SortVideoListUseCase
import com.learnwithsubs.video_list.domain.usecase.TranscodeVideoUseCase
import com.learnwithsubs.video_list.domain.usecase.VideoListUseCases
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class VideoListDomainModule {

    @Provides
    @Singleton
    fun provideVideoListUseCases(
        videoListRepository: com.learnwithsubs.video_list.domain.repository.VideoListRepository,
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