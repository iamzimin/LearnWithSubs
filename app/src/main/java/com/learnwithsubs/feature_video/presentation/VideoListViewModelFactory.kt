package com.learnwithsubs.feature_video.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learnwithsubs.feature_video.domain.usecase.VideoUseCases

class VideoListViewModelFactory(
    val videoUseCases: VideoUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideoListViewModel(
            videoUseCases = videoUseCases
        ) as T
    }
}