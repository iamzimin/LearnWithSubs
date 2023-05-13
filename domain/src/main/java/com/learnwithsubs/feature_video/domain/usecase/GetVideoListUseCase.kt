package com.learnwithsubs.feature_video.domain.usecase

import com.learnwithsubs.feature_video.domain.models.Video
import com.learnwithsubs.feature_video.domain.repository.VideoRepository
import com.learnwithsubs.feature_video.domain.util.OrderType
import com.learnwithsubs.feature_video.domain.util.VideoOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetVideoListUseCase(
    private val repository: VideoRepository
) {
    operator fun invoke(
        videoOrder: VideoOrder = VideoOrder.Date(OrderType.Descending)
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
            }
        }
    }
}