package com.example.yandex_dictionary_api.data.repository

import com.example.yandex_dictionary_api.domain.repository.TranslatorRepository
import com.example.yandex_dictionary_api.domain.service.TranslationService
import com.example.yandex_dictionary_api.models.DictionaryWordDTO
import com.google.android.gms.tasks.Task
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
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
    ): DictionaryWordDTO? {
        val apiService = yandexRetrofit.create(TranslationService::class.java)
        val wordResponse = apiService.getWordsFromDictionary(key = key, lang = lang, word = word).awaitResponse()

        if (wordResponse.isSuccessful) {
            val apiResponse = wordResponse.body()
            val definition = apiResponse?.def
            if (definition.isNullOrEmpty())
                return null

            val transl = definition[0].tr[0].text
            val dictionaryWordDTO = DictionaryWordDTO(
                translation = transl,
                synonyms = ArrayList()
            )

            for (defID in definition.indices) {
                val speechPart = definition[defID]
                val translations = speechPart.tr
                val pSpeech = com.example.yandex_dictionary_api.models.DictionarySynonyms(
                    id = 0,
                    word = "",
                    translation = "",
                    partSpeech = speechPart.pos,
                    type = com.example.yandex_dictionary_api.models.DictionaryType.PART_SPEECH
                )
                dictionaryWordDTO.synonyms.add(pSpeech)

                for (spID in translations.indices) {
                    val translation = translations[spID]
                    val syn: ArrayList<String> = ArrayList()
                    val mean: ArrayList<String> = ArrayList()
                    var dWord: com.example.yandex_dictionary_api.models.DictionarySynonyms


                    if (translation.syn != null && translation.mean != null) {
                        for (synonymID in translation.syn!!.indices) {
                            val synonym = translation.syn!![synonymID]
                            syn.add(synonym.text)
                        }
                        for (meanID in translation.mean!!.indices) {
                            val meaning = translation.mean!![meanID]
                            mean.add(meaning.text)
                        }
                        dWord = com.example.yandex_dictionary_api.models.DictionarySynonyms(
                            id = spID + 1,
                            word = mean.joinToString(", "),
                            translation = syn.joinToString(", "),
                            partSpeech = speechPart.pos,
                            type = com.example.yandex_dictionary_api.models.DictionaryType.WORD
                        )
                    }
                    else {
                        dWord = com.example.yandex_dictionary_api.models.DictionarySynonyms(
                            id = spID + 1,
                            word = translation.mean?.get(0)?.text ?: definition[defID].text,
                            translation = translation.text,
                            partSpeech = speechPart.pos,
                            type = com.example.yandex_dictionary_api.models.DictionaryType.WORD
                        )
                    }
                    dictionaryWordDTO.synonyms.add(dWord)
                }
            }
            return dictionaryWordDTO
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