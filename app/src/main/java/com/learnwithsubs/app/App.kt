package com.learnwithsubs.app

import android.app.Application
import com.learnwithsubs.feature_video_list.presentation.di.DaggerVideoListAppComponent
import com.learnwithsubs.feature_video_list.presentation.di.VideoListAppComponent
import com.learnwithsubs.feature_video_list.presentation.di.VideoListAppModule
import com.learnwithsubs.feature_video_view.presentation.di.DaggerVideoViewAppComponent
import com.learnwithsubs.feature_video_view.presentation.di.VideoViewAppComponent
import com.learnwithsubs.feature_video_view.presentation.di.VideoViewAppModule

class App : Application() {
    lateinit var videoListAppComponent: VideoListAppComponent
    lateinit var videoViewAppComponent: VideoViewAppComponent
    override fun onCreate() {
        super.onCreate()

        videoListAppComponent = DaggerVideoListAppComponent
            .builder()
            .videoListAppModule(VideoListAppModule(context = this))
            .build()

        videoViewAppComponent = DaggerVideoViewAppComponent
            .builder()
            .videoViewAppModule(VideoViewAppModule(context = this))
            .build()
    }
}