package com.learnwithsubs.video_view.domain.usecase

import com.learnwithsubs.video_view.domain.TranslationKeyAPI
import com.learnwithsubs.video_view.domain.models.TranslationModel
import com.learnwithsubs.video_view.domain.models.DictionaryWord
import com.learnwithsubs.video_view.domain.repository.TranslatorRepository

class GetWordsFromYandexDictionaryUseCase(
    private val yandexDictionaryRepository: TranslatorRepository
) {
    suspend fun invoke(model: TranslationModel): DictionaryWord? {
        return yandexDictionaryRepository.getWordsFromYandexDictionary(
            key = TranslationKeyAPI.YANDEX_DICTIONARY_KEY,
            lang = "${model.inputLanguage}-${model.outputLanguage}",
            word = model.word
        )
    }
}