package com.learnwithsubs.feature_video_view.repository

import com.google.android.gms.tasks.Task
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.learnwithsubs.feature_video_view.models.DictionarySynonyms
import com.learnwithsubs.feature_video_view.models.DictionaryType
import com.learnwithsubs.feature_video_view.models.DictionaryWord
import com.learnwithsubs.feature_video_view.service.TranslationService
import kotlinx.coroutines.CompletableDeferred
import retrofit2.Retrofit
import retrofit2.awaitResponse

class TranslatorRepositoryImpl(
    private val yandexRetrofit: Retrofit,
    private val serverRetrofit: Retrofit,
): TranslatorRepository {
    override suspend fun getWordsFromYandexDictionary(
        key: String,
        lang: String,
        word: String
    ): DictionaryWord? {
        val apiService = yandexRetrofit.create(TranslationService::class.java)
        val wordResponse = apiService.getWordsFromDictionary(key = key, lang = lang, word = word).awaitResponse()

        if (wordResponse.isSuccessful) {
            val apiResponse = wordResponse.body()
            val definition = apiResponse?.def
            if (definition.isNullOrEmpty())
                return null

            val transl = definition[0].tr[0].text
            val dictionaryWord = DictionaryWord(translation = transl, synonyms = ArrayList())

            for (defID in definition.indices) {
                val speechPart = definition[defID]
                val translations = speechPart.tr
                val pSpeech = DictionarySynonyms(id = 0, word = "", translation = "", partSpeech = speechPart.pos, type = DictionaryType.PART_SPEECH)
                dictionaryWord.synonyms.add(pSpeech)

                for (spID in translations.indices) {
                    val translation = translations[spID]
                    val syn: ArrayList<String> = ArrayList()
                    val mean: ArrayList<String> = ArrayList()
                    var dWord: DictionarySynonyms


                    if (translation.syn != null && translation.mean != null) {
                        for (synonymID in translation.syn!!.indices) {
                            val synonym = translation.syn!![synonymID]
                            syn.add(synonym.text)
                        }
                        for (meanID in translation.mean!!.indices) {
                            val meaning = translation.mean!![meanID]
                            mean.add(meaning.text)
                        }
                        dWord = DictionarySynonyms(
                            id = spID + 1,
                            word = mean.joinToString(", "),
                            translation = syn.joinToString(", "),
                            partSpeech = speechPart.pos,
                            type = DictionaryType.WORD
                        )
                    }
                    else {
                        dWord = DictionarySynonyms(
                            id = spID + 1,
                            word = translation.mean?.get(0)?.text ?: definition[defID].text,
                            translation = translation.text,
                            partSpeech = speechPart.pos,
                            type = DictionaryType.WORD
                        )
                    }
                    dictionaryWord.synonyms.add(dWord)
                }
            }
            return dictionaryWord
        } else {
            return null
        }
    }

    override suspend fun getWordsFromServerTranslator(
        word: String,
        fromLang: String,
        toLang: String
    ): String? {
        val apiService = serverRetrofit.create(TranslationService::class.java)
        val response = apiService.getWordsFromServerTranslator(word = word, fromLang = fromLang, toLang = toLang).awaitResponse()
        return response.body()
    }

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