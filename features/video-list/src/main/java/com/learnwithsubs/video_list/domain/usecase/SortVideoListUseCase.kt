package com.learnwithsubs.video_list.domain.usecase

import com.learnwithsubs.database.domain.models.Video
import com.learnwithsubs.general.util.OrderType
import com.example.yandex_dictionary_api.domain.util.VideoOrder

class SortVideoListUseCase {
    operator fun invoke(videoList: ArrayList<com.learnwithsubs.database.domain.models.Video>, sortMode: com.example.yandex_dictionary_api.domain.util.VideoOrder, filter: String?): List<com.learnwithsubs.database.domain.models.Video> {
        val sorted = when (sortMode.orderType) {
            is OrderType.Ascending -> {
                when (sortMode) {
                    is com.example.yandex_dictionary_api.domain.util.VideoOrder.Name -> videoList.sortedBy { it.name.lowercase() }
                    is com.example.yandex_dictionary_api.domain.util.VideoOrder.Date -> videoList.sortedBy { it.timestamp }
                    is com.example.yandex_dictionary_api.domain.util.VideoOrder.Duration -> videoList.sortedBy { it.duration }
                }
            }
            is OrderType.Descending -> {
                when (sortMode) {
                    is com.example.yandex_dictionary_api.domain.util.VideoOrder.Name -> videoList.sortedByDescending { it.name.lowercase() }
                    is com.example.yandex_dictionary_api.domain.util.VideoOrder.Date -> videoList.sortedByDescending { it.timestamp }
                    is com.example.yandex_dictionary_api.domain.util.VideoOrder.Duration -> videoList.sortedByDescending { it.duration }
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