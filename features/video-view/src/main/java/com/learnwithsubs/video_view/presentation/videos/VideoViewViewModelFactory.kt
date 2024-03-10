package com.learnwithsubs.video_view.presentation.videos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VideoViewViewModelFactory(
    val videoViewUseCases: com.learnwithsubs.video_view.domain.usecase.VideoViewUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideoViewViewModel(
            videoViewUseCases = videoViewUseCases
        ) as T
    }
}