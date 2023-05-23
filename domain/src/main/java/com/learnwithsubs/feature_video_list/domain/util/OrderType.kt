package com.learnwithsubs.feature_video_list.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
