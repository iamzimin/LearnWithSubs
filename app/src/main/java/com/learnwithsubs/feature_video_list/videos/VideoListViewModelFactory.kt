package com.learnwithsubs.feature_video_list.videos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learnwithsubs.feature_video_list.repository.VideoTranscodeRepository
import com.learnwithsubs.feature_video_list.usecase.VideoListUseCases

class VideoListViewModelFactory(
    val videoListUseCases: VideoListUseCases,
    val videoTranscodeRepository: VideoTranscodeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideoListViewModel(
            videoListUseCases = videoListUseCases,
            videoTranscodeRepository = videoTranscodeRepository
        ) as T
    }
}