package com.example.yandex_translator_api.domain.repository

import com.example.yandex_translator_api.models.DictionaryWordDTO

interface YandexTranslatorRepository {
    suspend fun getWordsFromYandexDictionary(key: String, lang: String, word: String): DictionaryWordDTO?
}