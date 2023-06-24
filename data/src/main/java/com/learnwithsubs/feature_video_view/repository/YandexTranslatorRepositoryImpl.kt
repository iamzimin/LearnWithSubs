package com.learnwithsubs.feature_video_view.repository

import com.learnwithsubs.feature_video_view.models.YandexIAmBodyRequest
import com.learnwithsubs.feature_video_view.models.YandexIAmBodyResponse
import com.learnwithsubs.feature_video_view.models.YandexTranslatorBody
import com.learnwithsubs.feature_video_view.models.YandexTranslatorResponse
import com.learnwithsubs.feature_video_view.service.YandexTranslatorService
import retrofit2.Call
import retrofit2.Retrofit

class YandexTranslatorRepositoryImpl(
    private val retrofitTranslator: Retrofit,
    private val retrofitIAmToken: Retrofit
): TranslatorRepository<YandexTranslatorResponse> {
    override fun getTranslation(
        contentType: String,
        authorization: String,
        body: YandexTranslatorBody
    ): Call<YandexTranslatorResponse> {
        val apiService = retrofitTranslator.create(YandexTranslatorService::class.java)
        return apiService.getTranslation(contentType = contentType, authorization = authorization, body = body)
    }

    override fun getYandexIAmToken(body: YandexIAmBodyRequest): Call<YandexIAmBodyResponse> {
        val apiService = retrofitIAmToken.create(YandexTranslatorService::class.java)
        return apiService.getYandexIAmToken(body = body)
    }
}