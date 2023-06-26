package com.learnwithsubs.feature_video_view.repository

import com.learnwithsubs.feature_video_view.models.server.YandexIAmBodyRequest
import com.learnwithsubs.feature_video_view.models.server.YandexIAmBodyResponse
import com.learnwithsubs.feature_video_view.models.server.YandexTranslatorBody
import com.learnwithsubs.feature_video_view.models.server.YandexTranslatorResponse
import retrofit2.Call

interface YandexTranslatorRepository {
    fun getYandexTranslation(contentType: String, authorization: String, body: YandexTranslatorBody): Call<YandexTranslatorResponse>
    fun getYandexIAmToken(body: YandexIAmBodyRequest): Call<YandexIAmBodyResponse>
    fun saveYandexIAmToken(iamtoken: String)
    fun getYandexIAmToken(): String?
    fun saveLastUpdateTimeYandexIAmToken(time: Long)
    fun getLastUpdateTimeYandexIAmToken(): Long
}