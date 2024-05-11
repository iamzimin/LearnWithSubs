package com.learnwithsubs.video_list.presentation.videos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learnwithsubs.video_list.domain.usecase.VideoListUseCases

class VideoListViewModelFactory(
    val videoListUseCases: VideoListUseCases,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideoListViewModel(
            videoListUseCases = videoListUseCases,
        ) as T
    }
}