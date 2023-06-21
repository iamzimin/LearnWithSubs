package com.learnwithsubs.feature_video_view.repository

import com.learnwithsubs.feature_video_view.models.TranslatorYandexBody
import retrofit2.Call

interface TranslatorRepository<T> {
    fun getTranslation(body: TranslatorYandexBody): Call<T>
}