package com.example.yandex_dictionary_api.domain

import com.example.yandex_dictionary_api.models.DictionaryElementDTO
import com.example.yandex_dictionary_api.models.DictionarySynonymsDTO
import com.example.yandex_dictionary_api.models.DictionaryWordDTO
import com.example.yandex_dictionary_api.models.YandexDictionaryResponse

internal fun YandexDictionaryResponse.DictionaryWordDTO() : DictionaryWordDTO {
    val definition = this.def

    val transl = definition[0].tr[0].text
    val dictionaryWordDTO = DictionaryWordDTO(
        translation = transl,
        dictionaryElement = ArrayList(),
    )

    for (defID in definition.indices) {
        val speechPart = definition[defID]
        val translations = speechPart.tr
        dictionaryWordDTO.dictionaryElement.add(DictionaryElementDTO.PartSpeech(partSpeech = speechPart.pos))

        for (spID in translations.indices) {
            val translation = translations[spID]
            val syn: ArrayList<String> = ArrayList()
            val mean: ArrayList<String> = ArrayList()
            var dWord: DictionarySynonymsDTO

            if (translation.syn != null && translation.mean != null) {
                for (synonymID in translation.syn.indices) {
                    val synonym = translation.syn[synonymID]
                    syn.add(synonym.text)
                }
                for (meanID in translation.mean.indices) {
                    val meaning = translation.mean[meanID]
                    mean.add(meaning.text)
                }
                dWord = DictionarySynonymsDTO(
                    id = spID + 1,
                    word = mean.joinToString(", "),
                    translation = syn.joinToString(", ")
                )
            }
            else {
                dWord = DictionarySynonymsDTO(
                    id = spID + 1,
                    word = translation.mean?.get(0)?.text ?: definition[defID].text,
                    translation = translation.text
                )
            }
            dictionaryWordDTO.dictionaryElement.add(DictionaryElementDTO.Synonyms(dictionarySynonymsDTO = dWord))
        }
    }
    return dictionaryWordDTO
}
