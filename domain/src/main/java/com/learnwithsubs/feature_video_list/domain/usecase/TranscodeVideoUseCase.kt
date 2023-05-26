package com.learnwithsubs.feature_video_list.domain.usecase

import android.content.Context
import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.repository.VideoTranscodeRepository

class TranscodeVideoUseCase(
    private val videoTranscodeRepository: VideoTranscodeRepository
) {
    suspend fun invoke(video: Video): Video? {
        return videoTranscodeRepository.transcodeVideo(video = video)
    }
}