package com.learnwithsubs.feature_video_view.domain.usecase

import com.learnwithsubs.feature_video_view.domain.models.DictionaryWord
import com.learnwithsubs.feature_video_view.domain.models.DictionaryYandexResponse
import com.learnwithsubs.feature_video_view.domain.repository.TranslatorRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetWordsFromDictionaryUseCase(
    private val translatorRepository: TranslatorRepository
) {
    fun invoke(): List<DictionaryWord> { //key: String, lang: String, word: String
        val dictionaryWordList: ArrayList<DictionaryWord> = ArrayList()

        translatorRepository.getWordsFromDictionary(
            key = "...",
            lang = "en-ru",
            word = "red"
        ).enqueue(object : Callback<DictionaryYandexResponse> {
            override fun onResponse(call: Call<DictionaryYandexResponse>, response: Response<DictionaryYandexResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val definition = apiResponse?.def

                    val transl = definition?.get(0)?.tr?.get(0)?.text
                    /*
                    definition?.forEachIndexed {  defID, speechPart ->
                        speechPart.tr.forEachIndexed { spID, translation ->
                            val syn: ArrayList<String> = ArrayList()
                            val mean: ArrayList<String> =  ArrayList()
                            translation.syn?.forEachIndexed { synonymID, synonym ->
                                syn.add(synonym.text)
                            }
                            translation.mean?.forEachIndexed { meanID, meaning ->
                                mean.add(meaning.text)
                            }
                            if (syn.isNotEmpty() && mean.isNotEmpty()) {
                                val word = DictionaryWord(spID + 1,
                                    syn.joinToString(", "),
                                    mean.joinToString(", "),
                                    speechPart.pos
                                )
                                dictionaryWordList.add(word)
                            }
                        }
                    }*/

                    if (definition != null) {
                        for (defID in definition.indices) {
                            val speechPart = definition[defID]
                            val translations = speechPart.tr

                            for (spID in translations.indices) {
                                val translation = translations[spID]
                                val syn: ArrayList<String> = ArrayList()
                                val mean: ArrayList<String> = ArrayList()
                                var word: DictionaryWord

                                if (translation.syn != null && translation.mean != null) {
                                    for (synonymID in translation.syn.indices) {
                                        val synonym = translation.syn[synonymID]
                                        syn.add(synonym.text)
                                    }
                                    for (meanID in translation.mean.indices) {
                                        val meaning = translation.mean[meanID]
                                        mean.add(meaning.text)
                                    }
                                    word = DictionaryWord(
                                        spID + 1,
                                        syn.joinToString(", "),
                                        mean.joinToString(", "),
                                        speechPart.pos
                                    )
                                }
                                else {
                                    word = DictionaryWord(
                                        spID + 1,
                                        translation.text,
                                        translation.mean?.get(0)?.text ?: translations[0].text,
                                        speechPart.pos
                                    )
                                }
                                dictionaryWordList.add(word)
                            }
                        }
                    }

                } else {
                    val tests = 1
                }
            }

            override fun onFailure(call: Call<DictionaryYandexResponse>, t: Throwable) {
                val tests = 1
            }
        })
        return dictionaryWordList
    }
}