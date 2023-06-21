package com.learnwithsubs.feature_video_list.di

import com.learnwithsubs.feature_video_list.VideoListActivity
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