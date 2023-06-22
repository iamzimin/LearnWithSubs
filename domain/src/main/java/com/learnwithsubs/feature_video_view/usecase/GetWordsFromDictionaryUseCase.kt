package com.learnwithsubs.feature_video_view.usecase

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.learnwithsubs.feature_video_view.models.DictionaryWord
import com.learnwithsubs.feature_video_view.models.DictionaryYandexResponse
import com.learnwithsubs.feature_video_view.models.DictionaryType
import com.learnwithsubs.feature_video_view.repository.DictionaryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetWordsFromDictionaryUseCase(
    private val context: Context,
    private val yandexDictionaryRepository: DictionaryRepository<DictionaryYandexResponse>
) {
    val translationLiveData: MutableLiveData<String?> = MutableLiveData()
    val dictionaryListLiveData: MutableLiveData<ArrayList<DictionaryWord>> = MutableLiveData()

    fun invoke(key: String, inputLang: Pair<String, String>, outputLang: Pair<String, String>, word: String) {
        val dictionaryWordList: ArrayList<DictionaryWord> = ArrayList()

        yandex(key = key, inputLang = inputLang, outputLang = outputLang, word = word, dictionaryWordList = dictionaryWordList)
    }


    private fun yandex(
        key: String,
        inputLang: Pair<String, String>,
        outputLang: Pair<String, String>,
        word: String,
        dictionaryWordList: ArrayList<DictionaryWord>
    ) {
        yandexDictionaryRepository.getWordsFromDictionary(
            key = key,
            lang = "${inputLang.second}-${outputLang.second}",
            word = word
        ).enqueue(object : Callback<DictionaryYandexResponse> {
            override fun onResponse(call: Call<DictionaryYandexResponse>, response: Response<DictionaryYandexResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val definition = apiResponse?.def
                    if (definition.isNullOrEmpty()){
                        translationLiveData.value = null
                        return
                    }

                    val transl = definition[0].tr[0].text
                    translationLiveData.value = transl

                    for (defID in definition.indices) {
                        val speechPart = definition[defID]
                        val translations = speechPart.tr
                        val pSpeech = DictionaryWord(id = 0, word = "", translation = "", partSpeech = speechPart.pos, type = DictionaryType.PART_SPEECH)
                        dictionaryWordList.add(pSpeech)

                        for (spID in translations.indices) {
                            val translation = translations[spID]
                            val syn: ArrayList<String> = ArrayList()
                            val mean: ArrayList<String> = ArrayList()
                            var dWord: DictionaryWord


                            if (translation.syn != null && translation.mean != null) {
                                for (synonymID in translation.syn.indices) {
                                    val synonym = translation.syn[synonymID]
                                    syn.add(synonym.text)
                                }
                                for (meanID in translation.mean.indices) {
                                    val meaning = translation.mean[meanID]
                                    mean.add(meaning.text)
                                }
                                dWord = DictionaryWord(
                                    id = spID + 1,
                                    word = mean.joinToString(", "),
                                    translation = syn.joinToString(", "),
                                    partSpeech = speechPart.pos,
                                    type = DictionaryType.WORD
                                )
                            }
                            else {
                                dWord = DictionaryWord(
                                    id = spID + 1,
                                    word = translation.mean?.get(0)?.text ?: translations[0].text,
                                    translation = translation.text,
                                    partSpeech = speechPart.pos,
                                    type = DictionaryType.WORD
                                )
                            }
                            dictionaryWordList.add(dWord)
                        }
                    }
                    dictionaryListLiveData.value = dictionaryWordList

                } else {
                    val tests = 1
                }
            }

            override fun onFailure(call: Call<DictionaryYandexResponse>, t: Throwable) {
                val tests = 1
            }
        })
    }
}