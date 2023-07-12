package com.learnwithsubs.general.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
