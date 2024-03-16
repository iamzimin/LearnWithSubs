package com.learnwithsubs.video_list.di

import com.learnwithsubs.VideoListActivity
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