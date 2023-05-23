package com.learnwithsubs.feature_video_list.presentation.di

import com.learnwithsubs.feature_video_list.presentation.VideoListActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        VideoAppModule::class,
        VideoDomainModule::class,
        VideoDataModule::class
    ]
)
interface VideoAppComponent {
    fun inject(videoListActivity: VideoListActivity)
}