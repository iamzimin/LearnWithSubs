package com.learnwithsubs.feature_video_view.repository

import com.learnwithsubs.feature_video_view.models.server.YandexTranslatorBody
import com.learnwithsubs.feature_video_view.models.server.YandexTranslatorResponse
import com.learnwithsubs.feature_video_view.service.YandexTranslatorService
import retrofit2.Call
import retrofit2.Retrofit

class YandexTranslatorRepositoryImpl(
    private val retrofitTranslator: Retrofit,
): TranslatorRepository<YandexTranslatorResponse> {
    override fun getTranslation(contentType: String, authorization: String, body: Any): Call<YandexTranslatorResponse> {
        val apiService = retrofitTranslator.create(YandexTranslatorService::class.java)
        return apiService.getTranslation(contentType = contentType, authorization = authorization, body = body as YandexTranslatorBody)
    }
}