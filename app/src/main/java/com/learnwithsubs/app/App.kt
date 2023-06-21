package com.learnwithsubs.app

import android.app.Application
import com.learnwithsubs.feature_video_list.di.DaggerVideoListAppComponent
import com.learnwithsubs.feature_video_list.di.VideoListAppComponent
import com.learnwithsubs.feature_video_list.di.VideoListAppModule
import com.learnwithsubs.feature_video_view.di.DaggerVideoViewAppComponent
import com.learnwithsubs.feature_video_view.di.VideoViewAppComponent
import com.learnwithsubs.feature_video_view.di.VideoViewAppModule

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