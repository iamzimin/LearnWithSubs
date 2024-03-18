package com.example.base.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
