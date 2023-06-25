package com.learnwithsubs.feature_video_view.service

import com.learnwithsubs.feature_video_view.models.server.YandexIAmBodyRequest
import com.learnwithsubs.feature_video_view.models.server.YandexIAmBodyResponse
import com.learnwithsubs.feature_video_view.models.server.YandexTranslatorBody
import com.learnwithsubs.feature_video_view.models.server.YandexTranslatorResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface YandexTranslatorService {
    @POST("translate/v2/translate")
    fun getTranslation(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") authorization: String,
        @Body body: YandexTranslatorBody
    ): Call<YandexTranslatorResponse>

    @POST("iam/v1/tokens")
    fun getYandexIAmToken(@Body body: YandexIAmBodyRequest): Call<YandexIAmBodyResponse>
}