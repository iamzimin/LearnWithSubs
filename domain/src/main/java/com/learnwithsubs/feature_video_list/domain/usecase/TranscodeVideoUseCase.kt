package com.learnwithsubs.feature_video_list.domain.usecase

import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.repository.VideoTranscodeRepository

class TranscodeVideoUseCase(
    private val videoTranscodeRepository: VideoTranscodeRepository,
    private val loadVideoUseCase: LoadVideoUseCase
) {
    suspend fun invoke(video: Video): Video? {
        return videoTranscodeRepository.transcodeVideo(video = video, loadVideoUseCase)
    }
}