package com.learnwithsubs.feature_video_list.usecase

import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.models.VideoErrorType
import com.learnwithsubs.feature_video_list.repository.ServerInteractionRepository
import com.learnwithsubs.feature_video_list.repository.VideoListRepository


class GetSubtitlesFromServerUseCase(
    private val serverInteractionRepository: ServerInteractionRepository,
    private val videoListRepository: VideoListRepository,
) {
    suspend fun invoke(video: Video): Video {
        val subtitles = serverInteractionRepository.getSubtitles(video = video)

        if (subtitles == null) {
            video.apply { errorType = VideoErrorType.GENERATING_SUBTITLES }
        } else {
            videoListRepository.saveSubtitles(video = video, subtitles = subtitles)
        }
        return video
    }
}