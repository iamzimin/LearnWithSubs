package com.learnwithsubs.video_list.domain.usecase

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.video_transcode.domain.models.VideoTranscode
import com.example.video_transcode.domain.repository.VideoTranscodeRepository
import com.learnwithsubs.video_list.domain.models.Video
import com.learnwithsubs.video_list.domain.toVideo

class GetVideoProgressLiveData(
    private val videoTranscodeRepository: VideoTranscodeRepository
) {
    fun invoke(): MutableLiveData<Video?> {
        val mutableLiveData: MutableLiveData<VideoTranscode?> = videoTranscodeRepository.getVideoProgressLiveData()
        val mediatorLiveData = MediatorLiveData<Video?>()
        mediatorLiveData.addSource(mutableLiveData) { videoTranscode ->
            mediatorLiveData.value = videoTranscode?.toVideo()
        }
        return mediatorLiveData
    }
}