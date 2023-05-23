package com.learnwithsubs.feature_video_list.presentation.app

import android.app.Application
import com.learnwithsubs.feature_video_list.presentation.di.DaggerVideoAppComponent
import com.learnwithsubs.feature_video_list.presentation.di.VideoAppComponent
import com.learnwithsubs.feature_video_list.presentation.di.VideoAppModule

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