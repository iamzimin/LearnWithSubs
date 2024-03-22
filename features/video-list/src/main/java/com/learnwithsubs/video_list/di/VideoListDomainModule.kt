package com.learnwithsubs.video_list.di

import com.example.server_api.domain.repository.ServerInteractionRepository
import com.example.video_transcode.domain.repository.VideoTranscodeRepository
import com.learnwithsubs.video_list.domain.repository.VideoListRepository
import com.learnwithsubs.video_list.domain.usecase.BackOldSubtitlesUseCase
import com.learnwithsubs.video_list.domain.usecase.CancelExtractAudio
import com.learnwithsubs.video_list.domain.usecase.CancelTranscodeVideo
import com.learnwithsubs.video_list.domain.usecase.DeleteVideoUseCase
import com.learnwithsubs.video_list.domain.usecase.ExtractAudioUseCase
import com.learnwithsubs.video_list.domain.usecase.ExtractVideoPreviewUseCase
import com.learnwithsubs.video_list.domain.usecase.GetLastVideoUseCase
import com.learnwithsubs.video_list.domain.usecase.GetSubtitlesFromServerUseCase
import com.learnwithsubs.video_list.domain.usecase.GetVideoListUseCase
import com.learnwithsubs.video_list.domain.usecase.GetVideoProgressLiveData
import com.learnwithsubs.video_list.domain.usecase.LoadNewSubtitlesUseCase
import com.learnwithsubs.video_list.domain.usecase.LoadVideoUseCase
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
            cancelExtractAudio = CancelExtractAudio(
                transcodeRepository
            ),
            cancelTranscodeVideo = CancelTranscodeVideo(
                transcodeRepository
            ),
            getVideoProgressLiveData = GetVideoProgressLiveData(
                transcodeRepository
            )
        )
    }

}