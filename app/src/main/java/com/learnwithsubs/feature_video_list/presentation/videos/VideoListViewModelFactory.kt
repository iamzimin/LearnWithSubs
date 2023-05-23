package com.learnwithsubs.feature_video_list.presentation.videos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learnwithsubs.feature_video_list.domain.usecase.VideoUseCases

class VideoListViewModelFactory(
    val videoUseCases: VideoUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideoListViewModel(
            videoUseCases = videoUseCases
        ) as T
    }
}