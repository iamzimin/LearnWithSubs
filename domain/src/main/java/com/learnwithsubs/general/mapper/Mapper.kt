package com.learnwithsubs.general.mapper

abstract class Mapper<From, To> {
    abstract fun map(from: From): To
}