package com.learnwithsubs.video_view.domain.usecase

import com.example.yandex_dictionary_api.domain.TranslationKeyAPI
import com.learnwithsubs.video_view.domain.models.TranslationModel
import com.example.yandex_dictionary_api.models.DictionaryWord
import com.example.yandex_dictionary_api.domain.TranslatorRepository

class GetWordsFromYandexDictionaryUseCase(
    private val yandexDictionaryRepository: com.example.yandex_dictionary_api.domain.TranslatorRepository
) {
    suspend fun invoke(model: TranslationModel): com.example.yandex_dictionary_api.models.DictionaryWord? {
        return yandexDictionaryRepository.getWordsFromYandexDictionary(
            key = com.example.yandex_dictionary_api.domain.TranslationKeyAPI.YANDEX_DICTIONARY_KEY,
            lang = "${model.inputLanguage}-${model.outputLanguage}",
            word = model.word
        )
    }
}