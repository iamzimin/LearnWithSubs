package com.learnwithsubs.shared_preference_settings.domain

import android.content.Context
import android.content.res.Configuration
import com.learnwithsubs.resource.R
import java.util.Locale


fun languageIdToString(id: Int, context: Context): Pair<String, String> {
    return when (id) {
        1 -> Pair(context.getString(R.string.english), "en")
        2 -> Pair(context.getString(R.string.russian), "ru")
        3 -> Pair(context.getString(R.string.spain), "es")
        4 -> Pair(context.getString(R.string.french), "fr")
        5 -> Pair(context.getString(R.string.japanese), "ja")
        6 -> Pair(context.getString(R.string.italian), "it")
        7 -> Pair(context.getString(R.string.german), "de")
        else -> throw IllegalArgumentException("Invalid language ID")
    }
}
fun stringToLanguageId(language: String, context: Context): Int {
    return when (language) {
        context.getString(R.string.english) -> 1
        context.getString(R.string.russian) -> 2
        context.getString(R.string.spain) -> 3
        context.getString(R.string.french) -> 4
        context.getString(R.string.japanese) -> 5
        context.getString(R.string.italian) -> 6
        context.getString(R.string.german) -> 7
        else -> throw IllegalArgumentException("Invalid language")
    }
}



fun styleIdToString(id: Int, context: Context): String {
    return when (id) {
        1 -> context.getString(R.string.light)
        2 -> context.getString(R.string.dark)
        else -> throw IllegalArgumentException("Invalid style ID")
    }
}
fun stringToStyleId(style: String, context: Context): Int {
    return when (style) {
        context.getString(R.string.light) -> 1
        context.getString(R.string.dark) -> 2
        else -> throw IllegalArgumentException("Invalid style")
    }
}



fun translatorSourceIdToString(id: Int, context: Context): String {
    return when (id) {
        1 -> context.getString(R.string.server)
        2 -> context.getString(R.string.yandex)
        3 -> context.getString(R.string.android)
        else -> throw IllegalArgumentException("Invalid Translator Source ID")
    }
}
fun stringToTranslatorSourceId(translatorSource: String, context: Context): Int {
    return when (translatorSource) {
        context.getString(R.string.server) -> 1
        context.getString(R.string.yandex) -> 2
        context.getString(R.string.android) -> 3
        else -> throw IllegalArgumentException("Invalid Translator Source")
    }
}