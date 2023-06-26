package com.learnwithsubs.feature_video_view.repository

import com.learnwithsubs.feature_video_view.models.server.YandexIAmBodyRequest
import com.learnwithsubs.feature_video_view.models.server.YandexIAmBodyResponse
import retrofit2.Call

interface YandexTokenRepository {
    fun getYandexIAmToken(body: YandexIAmBodyRequest): Call<YandexIAmBodyResponse>
    fun saveYandexIAmToken(iamtoken: String)
    fun getYandexIAmToken(): String?
    fun saveLastUpdateTimeYandexIAmToken(time: Long)
    fun getLastUpdateTimeYandexIAmToken(): Long
}