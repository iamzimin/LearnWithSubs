package com.learnwithsubs.feature_video_view.repository

import com.learnwithsubs.feature_video_view.models.server.YandexIAmBodyRequest
import com.learnwithsubs.feature_video_view.models.server.YandexIAmBodyResponse
import com.learnwithsubs.feature_video_view.models.server.YandexTranslatorBody
import retrofit2.Call

interface TranslatorRepository<T> {
    fun getTranslation(contentType: String, authorization: String, body: YandexTranslatorBody): Call<T>
    fun getYandexIAmToken(body: YandexIAmBodyRequest): Call<YandexIAmBodyResponse>
}