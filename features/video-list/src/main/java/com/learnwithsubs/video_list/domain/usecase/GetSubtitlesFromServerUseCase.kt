package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.database.domain.models.Video
import com.learnwithsubs.database.domain.models.VideoErrorType
import com.example.yandex_dictionary_api.domain.repository.ServerInteractionRepository
import com.learnwithsubs.database.domain.VideoListRepository


class GetSubtitlesFromServerUseCase(
    private val serverInteractionRepository: com.example.yandex_dictionary_api.domain.repository.ServerInteractionRepository,
    private val videoListRepository: com.learnwithsubs.database.domain.VideoListRepository,
) {
    suspend fun invoke(video: com.learnwithsubs.database.domain.models.Video): com.learnwithsubs.database.domain.models.Video {
        val subtitles = serverInteractionRepository.getSubtitles(video = video)

        if (subtitles == null) {
            video.apply { errorType = com.learnwithsubs.database.domain.models.VideoErrorType.GENERATING_SUBTITLES }
        } else {
            videoListRepository.saveSubtitles(video = video, subtitles = subtitles)
        }
        return video
    }
}