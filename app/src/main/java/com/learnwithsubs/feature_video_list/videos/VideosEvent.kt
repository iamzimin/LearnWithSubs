package com.learnwithsubs.feature_video_list.videos

import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.util.VideoOrder

sealed class VideosEvent {
    data class UpdateVideo(val video: Video): VideosEvent()
    data class SetOrderMode(val orderMode: VideoOrder): VideosEvent()
    data class Filter(val filter: String?): VideosEvent()
    data class UpdateVideoList(val videoOrder: VideoOrder?, val filter: String?): VideosEvent()
    data class DeSelect(val isNeedSelect: Boolean): VideosEvent()
    data class DeleteSelectedVideos(val videos: List<Video>?): VideosEvent()
    data class LoadVideo(val video: Video): VideosEvent()
    data class RenameVideo(val video: Video): VideosEvent()
}
