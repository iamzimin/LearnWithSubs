package com.learnwithsubs.feature_video_list.util

sealed class VideoOrder(var orderType: OrderType) {
    class Name(orderType: OrderType): VideoOrder(orderType)
    class Date(orderType: OrderType): VideoOrder(orderType)
    class Duration(orderType: OrderType): VideoOrder(orderType)
}
