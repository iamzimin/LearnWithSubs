package com.learnwithsubs.word_list.domain.util

import com.example.base.util.OrderType

sealed class WordOrder(var orderType: OrderType) {
    class Alphabet(orderType: OrderType): WordOrder(orderType)
    class Video(orderType: OrderType): WordOrder(orderType)
    class Date(orderType: OrderType): WordOrder(orderType)
}