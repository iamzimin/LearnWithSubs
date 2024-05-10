package com.learnwithsubs.shared_preference_settings.domain

import android.content.Context
import com.learnwithsubs.resource.R


fun languageIdToString(id: Int, context: Context): Pair<String, String> {
    return when (id) {
        1 -> Pair(context.getString(R.string.chinese), "zh")
        2 -> Pair(context.getString(R.string.english), "en")
        3 -> Pair(context.getString(R.string.french), "fr")
        4 -> Pair(context.getString(R.string.german), "de")
        5 -> Pair(context.getString(R.string.italian), "it")
        6 -> Pair(context.getString(R.string.japanese), "ja")
        7 -> Pair(context.getString(R.string.korean), "ko")
        8 -> Pair(context.getString(R.string.russian), "ru")
        9 -> Pair(context.getString(R.string.spain), "es")
        else -> throw IllegalArgumentException("Invalid language ID")
    }
}
fun stringToLanguageId(language: String, context: Context): Int {
    return when (language) {
        context.getString(R.string.chinese) -> 1
        context.getString(R.string.english) -> 2
        context.getString(R.string.french) -> 3
        context.getString(R.string.german) -> 4
        context.getString(R.string.italian) -> 5
        context.getString(R.string.japanese) -> 6
        context.getString(R.string.korean) -> 7
        context.getString(R.string.russian) -> 8
        context.getString(R.string.spain) -> 9
        else -> throw IllegalArgumentException("Invalid language")
    }
}



fun styleIdToString(id: Int, context: Context): String {
    return when (id) {
        1 -> context.getString(R.string.dark)
        2 -> context.getString(R.string.light)
        else -> throw IllegalArgumentException("Invalid style ID")
    }
}
fun stringToStyleId(style: String, context: Context): Int {
    return when (style) {
        context.getString(R.string.dark) -> 1
        context.getString(R.string.light) -> 2
        else -> throw IllegalArgumentException("Invalid style")
    }
}



fun translatorSourceIdToString(id: Int, context: Context): String {
    return when (id) {
        1 -> context.getString(R.string.yandex_plus_android)
        2 -> context.getString(R.string.yandex_plus_server)
        else -> throw IllegalArgumentException("Invalid Translator Source ID")
    }
}
fun stringToTranslatorSourceId(translatorSource: String, context: Context): Int {
    return when (translatorSource) {
        context.getString(R.string.yandex_plus_android) -> 1
        context.getString(R.string.yandex_plus_server) -> 2
        else -> throw IllegalArgumentException("Invalid Translator Source")
    }
}