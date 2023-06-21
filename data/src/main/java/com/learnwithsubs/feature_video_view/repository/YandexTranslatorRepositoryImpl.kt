package com.learnwithsubs.feature_video_view.repository

import com.learnwithsubs.feature_video_view.models.TranslatorYandexBody
import com.learnwithsubs.feature_video_view.models.TranslatorYandexResponse
import com.learnwithsubs.feature_video_view.service.YandexTranslatorService
import retrofit2.Call
import retrofit2.Retrofit

class YandexTranslatorRepositoryImpl(
    private val retrofit: Retrofit
): TranslatorRepository<TranslatorYandexResponse> {
    override fun getTranslation(body: TranslatorYandexBody): Call<TranslatorYandexResponse> {
        val apiService = retrofit.create(YandexTranslatorService::class.java)
        return apiService.getTranslation(body = body)
    }
}