package com.learnwithsubs.feature_video_view.usecase

import com.learnwithsubs.feature_video_view.TranslationKeyAPI
import com.learnwithsubs.feature_video_view.models.DictionaryModel
import com.learnwithsubs.feature_video_view.models.DictionarySynonyms
import com.learnwithsubs.feature_video_view.models.DictionaryWord
import com.learnwithsubs.feature_video_view.models.server.YandexDictionaryResponse
import com.learnwithsubs.feature_video_view.models.DictionaryType
import com.learnwithsubs.feature_video_view.repository.DictionaryRepository
import retrofit2.awaitResponse

class GetWordsFromYandexDictionaryUseCase(
    private val yandexDictionaryRepository: DictionaryRepository<YandexDictionaryResponse>
) {

    suspend fun invoke(model: DictionaryModel): DictionaryWord? {
        val wordResponse = yandexDictionaryRepository.getWordsFromDictionary(
            key = TranslationKeyAPI.YANDEX_DICTIONARY_KEY,
            lang = "${model.inputLanguage_ISO639_1}-${model.outputLanguage_ISO639_1}",
            word = model.word
        ).awaitResponse()
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
                        for (synonymID in translation.syn.indices) {
                            val synonym = translation.syn[synonymID]
                            syn.add(synonym.text)
                        }
                        for (meanID in translation.mean.indices) {
                            val meaning = translation.mean[meanID]
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
        }
        return null
    }
}