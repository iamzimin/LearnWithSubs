package com.learnwithsubs.mapper

abstract class Mapper<From, To> {
    abstract fun map(from: From): To
}