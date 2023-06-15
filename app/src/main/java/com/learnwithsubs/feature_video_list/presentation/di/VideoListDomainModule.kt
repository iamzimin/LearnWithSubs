package com.learnwithsubs.feature_video_list.presentation.di

import android.content.Context
import com.learnwithsubs.feature_video_list.domain.repository.ServerInteractionRepository
import com.learnwithsubs.feature_video_list.domain.repository.VideoListRepository
import com.learnwithsubs.feature_video_list.domain.repository.VideoTranscodeRepository
import com.learnwithsubs.feature_video_list.domain.usecase.DeleteVideoUseCase
import com.learnwithsubs.feature_video_list.domain.usecase.ExtractAudioUseCase
import com.learnwithsubs.feature_video_list.domain.usecase.GetLastVideoUseCase
import com.learnwithsubs.feature_video_list.domain.usecase.GetVideoListUseCase
import com.learnwithsubs.feature_video_list.domain.usecase.LoadVideoUseCase
import com.learnwithsubs.feature_video_list.domain.usecase.SendAudioToServerUseCase
import com.learnwithsubs.feature_video_list.domain.usecase.TranscodeVideoUseCase
import com.learnwithsubs.feature_video_list.domain.usecase.VideoListUseCases
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
            sendAudioToServerUseCase = SendAudioToServerUseCase(serverInteractionRepository)
        )
    }

}