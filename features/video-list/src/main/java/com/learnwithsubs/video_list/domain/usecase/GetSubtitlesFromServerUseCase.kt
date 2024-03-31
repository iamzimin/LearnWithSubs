package com.learnwithsubs.video_list.domain.usecase

import com.example.server_api.domain.repository.ServerInteractionRepository
import com.example.base.VideoConstants
import com.learnwithsubs.video_list.domain.models.Video
import com.learnwithsubs.video_list.domain.models.VideoErrorType
import com.learnwithsubs.video_list.domain.repository.VideoListRepository
import com.learnwithsubs.video_list.domain.toVideoDBO
import java.io.File


class GetSubtitlesFromServerUseCase(
    private val serverInteractionRepository: ServerInteractionRepository,
    private val videoListRepository: VideoListRepository,
) {
    suspend fun invoke(video: Video): Video {
        val file = File(video.outputPath, VideoConstants.EXTRACTED_AUDIO)
        val subtitles = serverInteractionRepository.getSubtitles(videoFile = file)

        if (subtitles == null) {
            video.apply { errorType = VideoErrorType.GENERATING_SUBTITLES }
        } else {
            videoListRepository.saveSubtitles(video = video.toVideoDBO(), subtitles = subtitles)
        }
        return video
    }
}