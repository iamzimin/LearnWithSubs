package com.learnwithsubs.server_translator_api.data.repository

import com.learnwithsubs.server_translator_api.domain.repository.ServerTranslatorRepository
import com.learnwithsubs.server_translator_api.domain.service.TranslationService
import retrofit2.Retrofit
import retrofit2.awaitResponse

class ServerTranslatorRepositoryImpl(
    private val serverRetrofit: Retrofit,
) : ServerTranslatorRepository {
    override suspend fun getWordsFromServerTranslator(
        word: String,
        fromLang: String,
        toLang: String
    ): String? {
        val apiService = serverRetrofit.create(TranslationService::class.java)
        val response = apiService.getWordsFromServerTranslator(word = word, fromLang = fromLang, toLang = toLang).awaitResponse()
        return response.body()
    }
}