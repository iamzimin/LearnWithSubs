package com.example.yandex_dictionary_api.domain

import com.example.yandex_dictionary_api.models.DictionaryTypeDTO
import com.example.yandex_dictionary_api.models.DictionaryWordDTO
import com.example.yandex_dictionary_api.models.YandexDictionaryResponse

internal fun YandexDictionaryResponse.DictionaryWordDTO() : DictionaryWordDTO {
    val definition = this.def

    val transl = definition[0].tr[0].text
    val dictionaryWordDTO = DictionaryWordDTO(
        translation = transl,
        synonyms = ArrayList()
    )

    for (defID in definition.indices) {
        val speechPart = definition[defID]
        val translations = speechPart.tr
        val pSpeech = com.example.yandex_dictionary_api.models.DictionarySynonymsDTO(
            id = 0,
            word = "",
            translation = "",
            partSpeech = speechPart.pos,
            type = DictionaryTypeDTO.PART_SPEECH
        )
        dictionaryWordDTO.synonyms.add(pSpeech)

        for (spID in translations.indices) {
            val translation = translations[spID]
            val syn: ArrayList<String> = ArrayList()
            val mean: ArrayList<String> = ArrayList()
            var dWord: com.example.yandex_dictionary_api.models.DictionarySynonymsDTO


            if (translation.syn != null && translation.mean != null) {
                for (synonymID in translation.syn.indices) {
                    val synonym = translation.syn[synonymID]
                    syn.add(synonym.text)
                }
                for (meanID in translation.mean.indices) {
                    val meaning = translation.mean[meanID]
                    mean.add(meaning.text)
                }
                dWord = com.example.yandex_dictionary_api.models.DictionarySynonymsDTO(
                    id = spID + 1,
                    word = mean.joinToString(", "),
                    translation = syn.joinToString(", "),
                    partSpeech = speechPart.pos,
                    type = DictionaryTypeDTO.WORD
                )
            }
            else {
                dWord = com.example.yandex_dictionary_api.models.DictionarySynonymsDTO(
                    id = spID + 1,
                    word = translation.mean?.get(0)?.text ?: definition[defID].text,
                    translation = translation.text,
                    partSpeech = speechPart.pos,
                    type = DictionaryTypeDTO.WORD
                )
            }
            dictionaryWordDTO.synonyms.add(dWord)
        }
    }
    return dictionaryWordDTO
}
