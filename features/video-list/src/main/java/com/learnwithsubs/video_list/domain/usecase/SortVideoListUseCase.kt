package com.learnwithsubs.video_list.domain.usecase

import com.example.base.util.OrderType
import com.example.base.util.VideoOrder
import com.learnwithsubs.video_list.domain.models.Video

class SortVideoListUseCase {
    operator fun invoke(videoList: ArrayList<Video>, sortMode: VideoOrder, filter: String?): List<Video> {
        val sorted = when (sortMode.orderType) {
            is OrderType.Ascending -> {
                when (sortMode) {
                    is VideoOrder.Name -> videoList.sortedBy { it.name.lowercase() }
                    is VideoOrder.Date -> videoList.sortedBy { it.timestamp }
                    is VideoOrder.Duration -> videoList.sortedBy { it.duration }
                }
            }
            is OrderType.Descending -> {
                when (sortMode) {
                    is VideoOrder.Name -> videoList.sortedByDescending { it.name.lowercase() }
                    is VideoOrder.Date -> videoList.sortedByDescending { it.timestamp }
                    is VideoOrder.Duration -> videoList.sortedByDescending { it.duration }
                }
            }
        }.filter { video ->
            filter?.let {
                video.name.lowercase().contains(it.lowercase())
            } ?: true
        }
        return sorted
    }
}