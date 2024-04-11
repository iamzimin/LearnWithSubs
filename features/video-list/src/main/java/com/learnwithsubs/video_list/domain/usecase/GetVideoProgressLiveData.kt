package com.learnwithsubs.video_list.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.example.video_transcode.domain.repository.VideoTranscodeRepository

class GetVideoProgressLiveData(
    private val videoTranscodeRepository: VideoTranscodeRepository
) {
    fun invoke(): MutableLiveData<Pair<Int?, Int>> {
        return videoTranscodeRepository.getVideoProgressLiveData()
    }
}