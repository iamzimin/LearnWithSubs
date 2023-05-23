package com.learnwithsubs.feature_video_list.presentation.app

import android.app.Application
import com.learnwithsubs.feature_video_list.presentation.di.DaggerVideoListAppComponent
import com.learnwithsubs.feature_video_list.presentation.di.VideoListAppComponent
import com.learnwithsubs.feature_video_list.presentation.di.VideoListAppModule

class VideoApp : Application() {
    lateinit var videoListAppComponent: VideoListAppComponent
    override fun onCreate() {
        super.onCreate()
        videoListAppComponent = DaggerVideoListAppComponent
            .builder()
            .videoListAppModule(VideoListAppModule(context = this))
            .build()
    }
}