package com.learnwithsubs.android_translator.data.repository

import com.google.android.gms.tasks.Task
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.learnwithsubs.android_translator.domain.repository.AndroidTranslatorRepository
import kotlinx.coroutines.CompletableDeferred

class AndroidTranslatorRepositoryImpl : AndroidTranslatorRepository {
    override suspend fun getWordsFromAndroidTranslator(
        word: String,
        fromLang: String,
        toLang: String
    ): String? {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(fromLang)
            .setTargetLanguage(toLang)
            .build()
        val translator = Translation.getClient(options)
        translator.downloadModelIfNeeded() //TODO delete
        val resultDeferred = CompletableDeferred<String?>()

        translator.translate(word).addOnCompleteListener { task: Task<String> ->
            if (task.isSuccessful) {
                val result = task.result
                resultDeferred.complete(result)
            } else {
                task.exception?.printStackTrace()
                resultDeferred.complete(null)
            }
            translator.close()
        }

        return try {
            resultDeferred.await()
        } catch (e: Exception) {
            null
        }
    }
}