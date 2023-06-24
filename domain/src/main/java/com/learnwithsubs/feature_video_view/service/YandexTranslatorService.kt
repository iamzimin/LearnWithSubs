package com.learnwithsubs.feature_video_view.service

import com.learnwithsubs.feature_video_view.TranslationKeyAPI
import com.learnwithsubs.feature_video_view.models.YandexIAmBodyRequest
import com.learnwithsubs.feature_video_view.models.YandexIAmBodyResponse
import com.learnwithsubs.feature_video_view.models.YandexTranslatorBody
import com.learnwithsubs.feature_video_view.models.YandexTranslatorResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface YandexTranslatorService {
//    @Headers("Content-Type: application/json", "Authorization: Bearer ${TranslationKeyAPI.YANDEX_IAM_TOKEN}")
//    @POST("translate/v2/translate")
//    fun getTranslation(@Body body: YandexTranslatorBody): Call<YandexTranslatorResponse>
    @POST("translate/v2/translate")
    fun getTranslation(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") authorization: String,
        @Body body: YandexTranslatorBody
    ): Call<YandexTranslatorResponse>

    @POST("iam/v1/tokens")
    fun getYandexIAmToken(@Body body: YandexIAmBodyRequest): Call<YandexIAmBodyResponse>
}