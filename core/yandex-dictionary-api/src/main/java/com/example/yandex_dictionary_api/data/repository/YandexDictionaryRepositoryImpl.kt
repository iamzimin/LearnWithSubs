package com.example.yandex_dictionary_api.data.repository

import com.example.yandex_dictionary_api.domain.DictionaryWordDTO
import com.example.yandex_dictionary_api.domain.repository.TranslatorRepository
import com.example.yandex_dictionary_api.domain.service.TranslationService
import com.example.yandex_dictionary_api.models.DictionaryWordDTO
import com.google.android.gms.tasks.Task
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.CompletableDeferred
import retrofit2.Retrofit
import retrofit2.awaitResponse

class TranslatorRepositoryImpl(
    private val yandexRetrofit: Retrofit,
    private val serverRetrofit: Retrofit,
): TranslatorRepository {
    override suspend fun getWordsFromYandexDictionary(
        key: String,
        lang: String,
        word: String
    ): DictionaryWordDTO? {
        val apiService = yandexRetrofit.create(TranslationService::class.java)
        val wordResponse = apiService.getWordsFromDictionary(key = key, lang = lang, word = word).awaitResponse()

        return if (wordResponse.isSuccessful) {
            val apiResponse = wordResponse.body() ?: return null
            return try {
                apiResponse.DictionaryWordDTO()
            } catch (e: Exception) { null }
        } else {
            null
        }
    }

    override suspend fun getWordsFromServerTranslator(
        word: String,
        fromLang: String,
        toLang: String
    ): String? {
        val apiService = serverRetrofit.create(TranslationService::class.java)
        val response = apiService.getWordsFromServerTranslator(word = word, fromLang = fromLang, toLang = toLang).awaitResponse()
        return response.body()
    }

    override suspend fun getWordsFromAndroidTranslator(
        word: String,
        fromLang: String,
        toLang: String
    ): String? {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(fromLang)
            .setTargetLanguage(toLang)
            .build()
        val translator = Translation.getClient(options)
        translator.downloadModelIfNeeded() //TODO delete
        val resultDeferred = CompletableDeferred<String?>()

        translator.translate(word).addOnCompleteListener { task: Task<String> ->
            if (task.isSuccessful) {
                val result = task.result
                resultDeferred.complete(result)
            } else {
                task.exception?.printStackTrace()
                resultDeferred.complete(null)
            }
            translator.close()
        }

        return try {
            resultDeferred.await()
        } catch (e: Exception) {
            null
        }
    }

}