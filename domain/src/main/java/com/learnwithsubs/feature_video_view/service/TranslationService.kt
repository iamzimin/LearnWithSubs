package com.learnwithsubs.feature_video_view.service

import com.learnwithsubs.feature_video_view.models.server.YandexDictionaryResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface TranslationService {
    @POST("dicservice.json/lookup")
    fun getWordsFromDictionary(
        @Query("key") key: String,
        @Query("lang") lang: String,
        @Query("text") word: String
    ): Call<YandexDictionaryResponse>

    @POST("/get_translation")
    fun getWordsFromServerTranslator(
        @Query("word") word: String,
        @Query("fromLang") fromLang: String,
        @Query("toLang") toLang: String
    ): Call<String>
}