package com.learnwithsubs.video_list.domain.usecase

import com.example.video_transcode.domain.repository.VideoTranscodeRepository
import com.learnwithsubs.video_list.domain.models.Video
import com.learnwithsubs.video_list.domain.toVideoTranscode

class ExtractVideoPreviewUseCase(private val videoTranscodeRepository: VideoTranscodeRepository) {
    suspend fun invoke(video: Video) {
        videoTranscodeRepository.extractPreview(videoTranscode = video.toVideoTranscode())
    }
}