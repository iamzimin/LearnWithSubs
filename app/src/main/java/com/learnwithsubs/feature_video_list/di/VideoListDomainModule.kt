package com.learnwithsubs.feature_video_list.di

import com.learnwithsubs.feature_video_list.repository.ServerInteractionRepository
import com.learnwithsubs.feature_video_list.repository.VideoListRepository
import com.learnwithsubs.feature_video_list.repository.VideoTranscodeRepository
import com.learnwithsubs.feature_video_list.usecase.BackOldSubtitlesUseCase
import com.learnwithsubs.feature_video_list.usecase.DeleteVideoUseCase
import com.learnwithsubs.feature_video_list.usecase.ExtractAudioUseCase
import com.learnwithsubs.feature_video_list.usecase.ExtractVideoPreviewUseCase
import com.learnwithsubs.feature_video_list.usecase.GetLastVideoUseCase
import com.learnwithsubs.feature_video_list.usecase.GetVideoListUseCase
import com.learnwithsubs.feature_video_list.usecase.LoadVideoUseCase
import com.learnwithsubs.feature_video_list.usecase.GetSubtitlesFromServerUseCase
import com.learnwithsubs.feature_video_list.usecase.LoadNewSubtitlesUseCase
import com.learnwithsubs.feature_video_list.usecase.SortVideoListUseCase
import com.learnwithsubs.feature_video_list.usecase.TranscodeVideoUseCase
import com.learnwithsubs.feature_video_list.usecase.VideoListUseCases
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
            getVideoListUseCase = GetVideoListUseCase(videoListRepository),
            deleteVideoUseCase = DeleteVideoUseCase(videoListRepository),
            loadVideoUseCase = LoadVideoUseCase(videoListRepository),
            getLastVideoUseCase = GetLastVideoUseCase(videoListRepository),
            transcodeVideoUseCase = TranscodeVideoUseCase(transcodeRepository),
            extractAudioUseCase = ExtractAudioUseCase(transcodeRepository),
            getSubtitlesFromServerUseCase = GetSubtitlesFromServerUseCase(serverInteractionRepository),
            extractVideoPreviewUseCase = ExtractVideoPreviewUseCase(transcodeRepository),
            sortVideoListUseCase = SortVideoListUseCase(),
            loadNewSubtitlesUseCase = LoadNewSubtitlesUseCase(videoListRepository),
            backOldSubtitlesUseCase = BackOldSubtitlesUseCase(videoListRepository),
        )
    }

}