package com.learnwithsubs.video_list.presentation.videos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VideoListViewModelFactory(
    val videoListUseCases: com.learnwithsubs.video_list.domain.usecase.VideoListUseCases,
    val videoTranscodeRepository: com.learnwithsubs.video_list.domain.repository.VideoTranscodeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideoListViewModel(
            videoListUseCases = videoListUseCases,
            videoTranscodeRepository = videoTranscodeRepository
        ) as T
    }
}