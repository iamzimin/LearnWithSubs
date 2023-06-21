package com.learnwithsubs.feature_video_view.di

import com.learnwithsubs.feature_video_view.VideoViewActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        VideoViewAppModule::class,
        VideoViewDomainModule::class,
        VideoViewDataModule::class
    ]
)
interface VideoViewAppComponent {
    fun inject(videoViewActivity: VideoViewActivity)
}