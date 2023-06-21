package com.learnwithsubs.feature_video_view.videos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learnwithsubs.feature_video_view.usecase.VideoViewUseCases

class VideoViewViewModelFactory(
    val videoViewUseCases: VideoViewUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideoViewViewModel(
            videoViewUseCases = videoViewUseCases
        ) as T
    }
}