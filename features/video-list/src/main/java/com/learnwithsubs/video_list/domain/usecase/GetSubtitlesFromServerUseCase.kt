package com.learnwithsubs.video_list.domain.usecase

import com.example.server_api.domain.repository.ServerInteractionRepository
import com.example.base.VideoConstants
import com.learnwithsubs.video_list.domain.models.Video
import com.learnwithsubs.video_list.domain.models.VideoErrorType
import com.learnwithsubs.video_list.domain.repository.VideoListRepository
import java.io.File


class GetSubtitlesFromServerUseCase(
    private val serverInteractionRepository: ServerInteractionRepository,
    private val videoListRepository: VideoListRepository,
) {
    suspend fun invoke(video: Video): Video {
        val file = File(video.outputPath, com.example.base.VideoConstants.EXTRACTED_AUDIO)
        val subtitles = serverInteractionRepository.getSubtitles(videoFile = file)

        if (subtitles == null) {
            video.apply { errorType = VideoErrorType.GENERATING_SUBTITLES }
        } else {
            val videoID = video.id ?: -1 //TODO
            videoListRepository.saveSubtitles(videoID = videoID, subtitles = subtitles)
        }
        return video
    }
}