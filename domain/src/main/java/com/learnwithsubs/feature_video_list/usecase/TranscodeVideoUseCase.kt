package com.learnwithsubs.feature_video_list.usecase

import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.repository.VideoTranscodeRepository

class TranscodeVideoUseCase(
    private val videoTranscodeRepository: VideoTranscodeRepository
) {
    suspend fun invoke(video: Video): Video? {
        return videoTranscodeRepository.transcodeVideo(video = video)
    }
}