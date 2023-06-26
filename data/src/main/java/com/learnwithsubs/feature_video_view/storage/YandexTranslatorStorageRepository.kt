package com.learnwithsubs.feature_video_view.storage

interface YandexTranslatorStorageRepository {
    fun saveYandexIAmToken(iamtoken: String)
    fun getYandexIAmToken(): String?
    fun saveLastUpdateTimeYandexIAmToken(time: Long)
    fun getLastUpdateTimeYandexIAmToken(): Long
}