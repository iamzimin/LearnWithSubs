package com.learnwithsubs.video_view.data.repository

import com.google.android.gms.tasks.Task
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.learnwithsubs.video_view.domain.models.DictionarySynonyms
import com.learnwithsubs.video_view.domain.models.DictionaryType
import com.learnwithsubs.video_view.domain.models.DictionaryWord
import com.learnwithsubs.video_view.domain.service.TranslationService
import kotlinx.coroutines.CompletableDeferred
import retrofit2.Retrofit
import retrofit2.awaitResponse

class TranslatorRepositoryImpl(
    private val yandexRetrofit: Retrofit,
    private val serverRetrofit: Retrofit,
): com.learnwithsubs.video_view.domain.repository.TranslatorRepository {
    override suspend fun getWordsFromYandexDictionary(
        key: String,
        lang: String,
        word: String
    ): com.learnwithsubs.video_view.domain.models.DictionaryWord? {
        val apiService = yandexRetrofit.create(com.learnwithsubs.video_view.domain.service.TranslationService::class.java)
        val wordResponse = apiService.getWordsFromDictionary(key = key, lang = lang, word = word).awaitResponse()

        if (wordResponse.isSuccessful) {
            val apiResponse = wordResponse.body()
            val definition = apiResponse?.def
            if (definition.isNullOrEmpty())
                return null

            val transl = definition[0].tr[0].text
            val dictionaryWord = com.learnwithsubs.video_view.domain.models.DictionaryWord(
                translation = transl,
                synonyms = ArrayList()
            )

            for (defID in definition.indices) {
                val speechPart = definition[defID]
                val translations = speechPart.tr
                val pSpeech = com.learnwithsubs.video_view.domain.models.DictionarySynonyms(
                    id = 0,
                    word = "",
                    translation = "",
                    partSpeech = speechPart.pos,
                    type = com.learnwithsubs.video_view.domain.models.DictionaryType.PART_SPEECH
                )
                dictionaryWord.synonyms.add(pSpeech)

                for (spID in translations.indices) {
                    val translation = translations[spID]
                    val syn: ArrayList<String> = ArrayList()
                    val mean: ArrayList<String> = ArrayList()
                    var dWord: com.learnwithsubs.video_view.domain.models.DictionarySynonyms


                    if (translation.syn != null && translation.mean != null) {
                        for (synonymID in translation.syn!!.indices) {
                            val synonym = translation.syn!![synonymID]
                            syn.add(synonym.text)
                        }
                        for (meanID in translation.mean!!.indices) {
                            val meaning = translation.mean!![meanID]
                            mean.add(meaning.text)
                        }
                        dWord = com.learnwithsubs.video_view.domain.models.DictionarySynonyms(
                            id = spID + 1,
                            word = mean.joinToString(", "),
                            translation = syn.joinToString(", "),
                            partSpeech = speechPart.pos,
                            type = com.learnwithsubs.video_view.domain.models.DictionaryType.WORD
                        )
                    }
                    else {
                        dWord = com.learnwithsubs.video_view.domain.models.DictionarySynonyms(
                            id = spID + 1,
                            word = translation.mean?.get(0)?.text ?: definition[defID].text,
                            translation = translation.text,
                            partSpeech = speechPart.pos,
                            type = com.learnwithsubs.video_view.domain.models.DictionaryType.WORD
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
        val apiService = serverRetrofit.create(com.learnwithsubs.video_view.domain.service.TranslationService::class.java)
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