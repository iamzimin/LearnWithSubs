package com.example.yandex_dictionary_api.domain.repository

import com.example.yandex_dictionary_api.models.DictionaryWordDTO

interface TranslatorRepository {
    suspend fun getWordsFromYandexDictionary(key: String, lang: String, word: String): DictionaryWordDTO?
    // suspend fun getWordsFromServerTranslator(word: String, fromLang: String, toLang: String): String?
    // suspend fun getWordsFromAndroidTranslator(word: String, fromLang: String, toLang: String): String?
}