package com.learnwithsubs.feature_video_list.usecase

import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.repository.VideoListRepository
import com.learnwithsubs.feature_video_list.util.OrderType
import com.learnwithsubs.feature_video_list.util.VideoOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class GetVideoListUseCase(
    private val repository: VideoListRepository
) {
    operator fun invoke(
        videoOrder: VideoOrder = VideoOrder.Date(OrderType.Descending),
        filter: String?,
    ) : Flow<List<Video>> {
        return repository.getVideos().map { videos ->
            when(videoOrder.orderType) {
                is OrderType.Ascending -> {
                    when(videoOrder) {
                        is VideoOrder.Name -> videos.sortedBy {it.name.lowercase()}
                        is VideoOrder.Date -> videos.sortedBy {it.timestamp}
                        is VideoOrder.Duration -> videos.sortedBy {it.duration}
                    }
                }
                is OrderType.Descending -> {
                    when(videoOrder) {
                        is VideoOrder.Name -> videos.sortedByDescending {it.name.lowercase()}
                        is VideoOrder.Date -> videos.sortedByDescending {it.timestamp}
                        is VideoOrder.Duration -> videos.sortedByDescending {it.duration}
                    }
                }
            }.filter { video ->
                filter?.let {
                    video.name.lowercase().contains(it.lowercase())
                } ?: true
            }
        }
    }
}