package com.learnwithsubs.feature_video_list.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
