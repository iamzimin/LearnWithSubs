package com.learnwithsubs.feature_video_list.presentation.di

import com.learnwithsubs.feature_video_list.presentation.VideoListActivity
import com.learnwithsubs.feature_video_view.presentation.di.VideoViewAppModule
import com.learnwithsubs.feature_video_view.presentation.di.VideoViewDataModule
import com.learnwithsubs.feature_video_view.presentation.di.VideoViewDomainModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        VideoListAppModule::class,
        VideoListDomainModule::class,
        VideoListDataModule::class,
    ]
)
interface VideoListAppComponent {
    fun inject(videoListActivity: VideoListActivity)
}