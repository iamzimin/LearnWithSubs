package com.learnwithsubs.server_translator_api.domain.service

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface TranslationService {
    @POST("/get_translation")
    fun getWordsFromServerTranslator(
        @Query("word") word: String,
        @Query("fromLang") fromLang: String,
        @Query("toLang") toLang: String
    ): Call<String>
}