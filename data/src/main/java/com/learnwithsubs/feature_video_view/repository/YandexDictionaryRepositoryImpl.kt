package com.learnwithsubs.feature_video_view.repository

import com.learnwithsubs.feature_video_view.models.server.YandexDictionaryResponse
import com.learnwithsubs.feature_video_view.service.YandexDictionaryService
import retrofit2.Call
import retrofit2.Retrofit

class YandexDictionaryRepositoryImpl(
    private val retrofit: Retrofit
): DictionaryRepository<YandexDictionaryResponse> {
    override fun getWordsFromDictionary(key: String, lang: String, word: String): Call<YandexDictionaryResponse> {
        val apiService = retrofit.create(YandexDictionaryService::class.java)
        return apiService.getWordsFromDictionary(key, lang, word)
//        val request: Request = call.request()
//        val requestUrl: HttpUrl = request.url
    }
}