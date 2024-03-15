package com.example.yandex_dictionary_api.domain.util

import com.learnwithsubs.general.util.OrderType

sealed class VideoOrder(var orderType: OrderType) {
    class Name(orderType: OrderType): VideoOrder(orderType)
    class Date(orderType: OrderType): VideoOrder(orderType)
    class Duration(orderType: OrderType): VideoOrder(orderType)
}
