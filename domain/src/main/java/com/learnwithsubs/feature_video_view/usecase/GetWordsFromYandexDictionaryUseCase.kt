package com.learnwithsubs.feature_video_view.usecase

import com.learnwithsubs.feature_video_view.TranslationKeyAPI
import com.learnwithsubs.feature_video_view.models.TranslationModel
import com.learnwithsubs.feature_video_view.models.DictionaryWord
import com.learnwithsubs.feature_video_view.repository.TranslatorRepository

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