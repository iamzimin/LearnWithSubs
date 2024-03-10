package com.learnwithsubs

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
