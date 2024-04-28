package com.learnwithsubs.server_translator_api.domain.repository

interface ServerTranslatorRepository {
    suspend fun getWordsFromServerTranslator(word: String, fromLang: String, toLang: String): String?
}