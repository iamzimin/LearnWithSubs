package com.learnwithsubs.feature_video.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
