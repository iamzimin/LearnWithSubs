package com.learnwithsubs.feature_video_view.service

import com.learnwithsubs.feature_video_view.TranslationKeyAPI
import com.learnwithsubs.feature_video_view.models.TranslatorYandexBody
import com.learnwithsubs.feature_video_view.models.TranslatorYandexResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface YandexTranslatorService {
    @Headers("Content-Type: application/json", "Authorization: Bearer ${TranslationKeyAPI.YANDEX_IAM_TOKEN}")
    @POST("translate/v2/translate")
    fun getTranslation(
        @Body body: TranslatorYandexBody
    ): Call<TranslatorYandexResponse>
}