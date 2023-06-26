package com.learnwithsubs.feature_video_view.storage.sharedprefs

import android.content.Context
import android.widget.Toast
import com.learnwithsubs.feature_video_view.storage.YandexTranslatorStorageRepository

private const val SHARED_PREFS_NAME = "Translation_storage"

class SharedPrefsYandexTranslatorStorageRepository(private val context: Context) :
    YandexTranslatorStorageRepository {
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveYandexIAmToken(iamtoken: String) {
        sharedPreferences.edit().putString("YandexIAmToken", iamtoken).apply()
        Toast.makeText(context, "New Token", Toast.LENGTH_SHORT).show()
    }

    override fun getYandexIAmToken(): String? {
        return sharedPreferences.getString("YandexIAmToken", null)
    }

    override fun saveLastUpdateTimeYandexIAmToken(time: Long) {
        sharedPreferences.edit().putLong("lastUpdateTimeYandexIAmToken", time).apply()
    }

    override fun getLastUpdateTimeYandexIAmToken(): Long {
        return sharedPreferences.getLong("lastUpdateTimeYandexIAmToken", 0)
    }
}