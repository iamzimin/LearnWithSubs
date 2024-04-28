package com.example.yandex_translator_api.domain.service

import com.example.yandex_translator_api.models.YandexDictionaryResponse
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
}