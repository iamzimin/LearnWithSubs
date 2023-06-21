package com.learnwithsubs.feature_video_view.repository

import retrofit2.Call

interface TranslatorRepository<T> {
    fun getWordsFromDictionary(key: String, lang: String, word: String): Call<T>
    fun getTranslation(word: String): String
}