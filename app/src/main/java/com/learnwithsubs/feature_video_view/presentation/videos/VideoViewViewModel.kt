package com.learnwithsubs.feature_video_view.presentation.videos

import androidx.lifecycle.ViewModel
import com.learnwithsubs.feature_video_view.domain.usecase.VideoViewUseCases
import javax.inject.Inject

class VideoViewViewModel @Inject constructor(
    val videoViewUseCases: VideoViewUseCases
) : ViewModel() {
}
