package com.learnwithsubs.video_view.domain.repository

import com.learnwithsubs.video_view.domain.models.DictionaryWord

interface TranslatorRepository {
    suspend fun getWordsFromYandexDictionary(key: String, lang: String, word: String): DictionaryWord?
    suspend fun getWordsFromServerTranslator(word: String, fromLang: String, toLang: String): String?
    suspend fun getWordsFromAndroidTranslator(word: String, fromLang: String, toLang: String): String?
}