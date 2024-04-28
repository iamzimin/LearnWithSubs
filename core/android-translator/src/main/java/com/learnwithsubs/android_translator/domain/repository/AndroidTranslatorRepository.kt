package com.learnwithsubs.android_translator.domain.repository

interface AndroidTranslatorRepository {
    suspend fun getWordsFromAndroidTranslator(word: String, fromLang: String, toLang: String): String?
}