package com.learnwithsubs.feature_video_view.repository

import retrofit2.Call

interface TranslatorRepository<T> {
    fun getTranslation(contentType: String, authorization: String, body: Any): Call<T>
}