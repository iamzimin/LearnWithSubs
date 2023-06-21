package com.learnwithsubs.feature_video_view.repository

import com.learnwithsubs.feature_video_view.models.DictionaryYandexResponse
import com.learnwithsubs.feature_video_view.repository.TranslatorRepository
import com.learnwithsubs.feature_video_view.repository.YandexTranslationService
import retrofit2.Call
import retrofit2.Retrofit

class YandexTranslatorRepositoryImpl(
    private val retrofit: Retrofit
): TranslatorRepository<DictionaryYandexResponse> {
    override fun getWordsFromDictionary(key: String, lang: String, word: String): Call<DictionaryYandexResponse> {
        val apiService = retrofit.create(YandexTranslationService::class.java)
        return apiService.getWordsFromDictionary(key, lang, word)
//        val request: Request = call.request()
//        val requestUrl: HttpUrl = request.url
    }

    override fun getTranslation(word: String): String {
        TODO("Not yet implemented")
    }
}