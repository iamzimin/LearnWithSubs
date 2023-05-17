package com.learnwithsubs.feature_video.presentation.app

import android.app.Application
import com.learnwithsubs.feature_video.presentation.di.DaggerVideoAppComponent
import com.learnwithsubs.feature_video.presentation.di.VideoAppComponent
import com.learnwithsubs.feature_video.presentation.di.VideoAppModule

class VideoApp : Application() {
    lateinit var videoAppComponent: VideoAppComponent
    override fun onCreate() {
        super.onCreate()
        videoAppComponent = DaggerVideoAppComponent
            .builder()
            .videoAppModule(VideoAppModule(context = this))
            .build()
    }
}