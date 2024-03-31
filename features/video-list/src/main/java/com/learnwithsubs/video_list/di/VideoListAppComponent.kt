package com.learnwithsubs.video_list.di

import com.learnwithsubs.video_list.presentation.VideoListFragment
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
internal interface VideoListAppComponent {
    fun inject(videoListFragment: VideoListFragment)
}