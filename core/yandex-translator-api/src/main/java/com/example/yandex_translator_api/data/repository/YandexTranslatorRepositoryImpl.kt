package com.example.yandex_translator_api.data.repository

import android.util.Log
import com.example.yandex_translator_api.domain.DictionaryWordDTO
import com.example.yandex_translator_api.domain.repository.YandexTranslatorRepository
import com.example.yandex_translator_api.domain.service.TranslationService
import com.example.yandex_translator_api.models.DictionaryWordDTO
import retrofit2.Retrofit
import retrofit2.awaitResponse

class YandexTranslatorRepositoryImpl(
    private val yandexRetrofit: Retrofit,
): YandexTranslatorRepository {
    override suspend fun getWordsFromYandexDictionary(
        key: String,
        lang: String,
        word: String
    ): DictionaryWordDTO? {
        val apiService = yandexRetrofit.create(TranslationService::class.java)

        try {
            val wordResponse = apiService.getWordsFromDictionary(key = key, lang = lang, word = word).awaitResponse()

            return if (wordResponse.isSuccessful) {
                val apiResponse = wordResponse.body() ?: return null
                return try {
                    apiResponse.DictionaryWordDTO()
                } catch (e: Exception) {
                    Log.e("Error", "There is no such word in the dictionary: $word")
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("Error", "API not available")
            return null
        }

    }
}