package com.learnwithsubs.feature_video_view.repository

import com.learnwithsubs.feature_video_view.models.DictionaryWord
import retrofit2.Call

interface TranslatorRepository {
    suspend fun getWordsFromYandexDictionary(key: String, lang: String, word: String): DictionaryWord?
    suspend fun getWordsFromServerTranslator(word: String, fromLang: String, toLang: String): String?
    fun getWordsFromAndroidTranslator(): Call<String>
}