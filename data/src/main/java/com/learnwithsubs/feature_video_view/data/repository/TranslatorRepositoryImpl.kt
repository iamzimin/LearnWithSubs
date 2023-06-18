package com.learnwithsubs.feature_video_view.data.repository

import com.learnwithsubs.feature_video_view.domain.models.DictionaryYandexResponse
import com.learnwithsubs.feature_video_view.domain.repository.TranslatorRepository
import retrofit2.Call
import retrofit2.Retrofit

class TranslatorRepositoryImpl(
    private val retrofit: Retrofit
): TranslatorRepository {
    override fun getWordsFromDictionary(key: String, lang: String, word: String): Call<DictionaryYandexResponse> {
        val apiService = retrofit.create(TranslatorRepository::class.java)
        val call = apiService.getWordsFromDictionary(key, lang, word)
//        val request: Request = call.request()
//        val requestUrl: HttpUrl = request.url
        return call
    }

    override fun getTranslation(word: String): String {
        TODO("Not yet implemented")
    }
}