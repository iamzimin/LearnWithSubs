package com.learnwithsubs.app

import android.app.Application
import com.learnwithsubs.feature_video_list.di.VideoListAppComponent
import com.learnwithsubs.feature_video_list.di.DaggerVideoListAppComponent
import com.learnwithsubs.feature_video_list.di.VideoListAppModule
import com.learnwithsubs.feature_video_view.di.VideoViewAppComponent
import com.learnwithsubs.feature_video_view.di.DaggerVideoViewAppComponent
import com.learnwithsubs.feature_video_view.di.VideoViewAppModule
import com.learnwithsubs.feature_word_list.di.WordListAppComponent
import com.learnwithsubs.feature_word_list.di.DaggerWordListAppComponent
import com.learnwithsubs.feature_word_list.di.WordListAppModule

class App : Application() {
    lateinit var videoListAppComponent: VideoListAppComponent
    lateinit var videoViewAppComponent: VideoViewAppComponent
    lateinit var wordListAppComponent: WordListAppComponent
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

        wordListAppComponent = DaggerWordListAppComponent
            .builder()
            .wordListAppModule(WordListAppModule(context = this))
            .build()
    }
}