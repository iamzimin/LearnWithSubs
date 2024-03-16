package com.learnwithsubs.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
