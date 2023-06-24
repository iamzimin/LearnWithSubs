package com.learnwithsubs.feature_video_view.repository

import com.learnwithsubs.feature_video_view.models.YandexIAmBodyRequest
import com.learnwithsubs.feature_video_view.models.YandexIAmBodyResponse
import com.learnwithsubs.feature_video_view.models.YandexTranslatorBody
import retrofit2.Call

interface TranslatorRepository<T> {
    fun getTranslation(contentType: String, authorization: String, body: YandexTranslatorBody): Call<T>
    fun getYandexIAmToken(body: YandexIAmBodyRequest): Call<YandexIAmBodyResponse>
}