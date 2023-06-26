package com.learnwithsubs.feature_video_view.repository

import com.learnwithsubs.feature_video_view.models.server.YandexIAmBodyRequest
import com.learnwithsubs.feature_video_view.models.server.YandexIAmBodyResponse
import com.learnwithsubs.feature_video_view.service.YandexTranslatorService
import com.learnwithsubs.feature_video_view.storage.YandexTranslatorStorageRepository
import retrofit2.Call
import retrofit2.Retrofit

class YandexTokenRepositoryImpl(
    private val retrofitIAmToken: Retrofit,
    private val sharedPrefsYandexTranslator: YandexTranslatorStorageRepository,
): YandexTokenRepository {
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