package com.learnwithsubs.feature_video_view.repository

import com.learnwithsubs.feature_video_view.models.server.YandexIAmBodyRequest
import com.learnwithsubs.feature_video_view.models.server.YandexIAmBodyResponse
import com.learnwithsubs.feature_video_view.models.server.YandexTranslatorBody
import com.learnwithsubs.feature_video_view.models.server.YandexTranslatorResponse
import com.learnwithsubs.feature_video_view.service.YandexTranslatorService
import com.learnwithsubs.feature_video_view.storage.YandexTranslatorStorageRepository
import retrofit2.Call
import retrofit2.Retrofit

class YandexTranslatorRepositoryImpl(
    private val retrofitTranslator: Retrofit,
    private val retrofitIAmToken: Retrofit,
    private val sharedPrefsYandexTranslator: YandexTranslatorStorageRepository,
): YandexTranslatorRepository {
    override fun getYandexTranslation(contentType: String, authorization: String, body: YandexTranslatorBody): Call<YandexTranslatorResponse> {
        val apiService = retrofitTranslator.create(YandexTranslatorService::class.java)
        return apiService.getTranslation(contentType = contentType, authorization = authorization, body = body)
    }

    override fun getYandexIAmToken(body: YandexIAmBodyRequest): Call<YandexIAmBodyResponse> {
        val apiService = retrofitIAmToken.create(YandexTranslatorService::class.java)
        return apiService.getYandexIAmToken(body = body)
    }

    override fun getYandexIAmToken(): String? {
        return sharedPrefsYandexTranslator.getYandexIAmToken()
    }

    override fun saveYandexIAmToken(iamtoken: String) {
        sharedPrefsYandexTranslator.saveYandexIAmToken(iamtoken = iamtoken)
    }

    override fun saveLastUpdateTimeYandexIAmToken(time: Long) {
        sharedPrefsYandexTranslator.saveLastUpdateTimeYandexIAmToken(time = time)
    }

    override fun getLastUpdateTimeYandexIAmToken(): Long {
        return sharedPrefsYandexTranslator.getLastUpdateTimeYandexIAmToken()
    }
}