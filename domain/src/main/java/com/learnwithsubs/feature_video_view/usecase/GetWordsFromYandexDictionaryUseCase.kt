package com.learnwithsubs.feature_video_view.usecase

import com.learnwithsubs.feature_video_view.TranslationKeyAPI
import com.learnwithsubs.feature_video_view.models.DictionaryModel
import com.learnwithsubs.feature_video_view.models.DictionarySynonyms
import com.learnwithsubs.feature_video_view.models.DictionaryWord
import com.learnwithsubs.feature_video_view.models.DictionaryType
import com.learnwithsubs.feature_video_view.repository.TranslatorRepository
import retrofit2.awaitResponse

class GetWordsFromYandexDictionaryUseCase(
    private val yandexDictionaryRepository: TranslatorRepository
) {
    suspend fun invoke(model: DictionaryModel): DictionaryWord? {
        return yandexDictionaryRepository.getWordsFromYandexDictionary(
            key = TranslationKeyAPI.YANDEX_DICTIONARY_KEY,
            lang = "${model.inputLanguage_ISO639_1}-${model.outputLanguage_ISO639_1}",
            word = model.word
        )
    }
}