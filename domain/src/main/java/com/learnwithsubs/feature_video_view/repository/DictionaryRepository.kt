package com.learnwithsubs.feature_video_view.repository

import retrofit2.Call

interface DictionaryRepository<T> {
    fun getWordsFromDictionary(key: String, lang: String, word: String): Call<T>
}