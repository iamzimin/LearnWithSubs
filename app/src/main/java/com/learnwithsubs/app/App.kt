package com.learnwithsubs.app

import android.app.Application
import com.learnwithsubs.video_list.presentation.di.VideoListAppComponent
import com.learnwithsubs.feature_video_list.di.DaggerVideoListAppComponent
import com.learnwithsubs.video_list.presentation.di.VideoListAppModule
import com.learnwithsubs.video_view.presentation.di.VideoViewAppComponent
import com.learnwithsubs.feature_video_view.di.DaggerVideoViewAppComponent
import com.learnwithsubs.video_view.presentation.di.VideoViewAppModule
import com.learnwithsubs.word_list.presentation.di.WordListAppComponent
import com.learnwithsubs.feature_word_list.di.DaggerWordListAppComponent
import com.learnwithsubs.word_list.presentation.di.WordListAppModule

class App : Application() {
    lateinit var videoListAppComponent: com.learnwithsubs.video_list.presentation.di.VideoListAppComponent
    lateinit var videoViewAppComponent: com.learnwithsubs.video_view.presentation.di.VideoViewAppComponent
    lateinit var wordListAppComponent: com.learnwithsubs.word_list.presentation.di.WordListAppComponent
    override fun onCreate() {
        super.onCreate()

        videoListAppComponent = DaggerVideoListAppComponent
            .builder()
            .videoListAppModule(
                com.learnwithsubs.video_list.presentation.di.VideoListAppModule(
                    context = this
                )
            )
            .build()

        videoViewAppComponent = DaggerVideoViewAppComponent
            .builder()
            .videoViewAppModule(
                com.learnwithsubs.video_view.presentation.di.VideoViewAppModule(
                    context = this
                )
            )
            .build()

        wordListAppComponent = DaggerWordListAppComponent
            .builder()
            .wordListAppModule(com.learnwithsubs.word_list.presentation.di.WordListAppModule(context = this))
            .build()
    }
}