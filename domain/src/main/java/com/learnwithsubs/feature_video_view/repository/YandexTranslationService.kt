package com.learnwithsubs.feature_video_view.repository

import com.learnwithsubs.feature_video_view.models.DictionaryYandexResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface YandexTranslationService {
    @POST("dicservice.json/lookup")
    fun getWordsFromDictionary(
        @Query("key") key: String,
        @Query("lang") lang: String,
        @Query("text") word: String
    ): Call<DictionaryYandexResponse>
}