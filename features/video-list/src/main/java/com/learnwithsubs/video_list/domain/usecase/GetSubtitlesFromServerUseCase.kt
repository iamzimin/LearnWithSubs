package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.database.domain.models.Video
import com.learnwithsubs.database.domain.models.VideoErrorType
import com.example.yandex_dictionary_api.domain.repository.ServerInteractionRepository
import com.learnwithsubs.database.domain.VideoListRepository
import com.learnwithsubs.video_list.domain.repository.VideoListRepository


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