package com.learnwithsubs.video_view.domain.usecase

import com.example.yandex_dictionary_api.domain.TranslationKeyAPI
import com.learnwithsubs.video_view.domain.models.TranslationModel
import com.example.yandex_dictionary_api.domain.repository.TranslatorRepository
import com.example.yandex_dictionary_api.models.DictionaryWordDTO

class GetWordsFromYandexDictionaryUseCase(
    private val yandexDictionaryRepository: TranslatorRepository
) {
    suspend fun invoke(model: TranslationModel): DictionaryWordDTO? { //TODO mapper?
        return yandexDictionaryRepository.getWordsFromYandexDictionary(
            key = TranslationKeyAPI.YANDEX_DICTIONARY_KEY,
            lang = "${model.inputLanguage}-${model.outputLanguage}",
            word = model.word
        )
    }
}