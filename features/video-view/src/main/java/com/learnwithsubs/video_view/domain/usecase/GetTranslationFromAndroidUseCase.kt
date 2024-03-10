package com.learnwithsubs.video_view.domain.usecase

import com.learnwithsubs.video_view.domain.models.TranslationModel
import com.example.yandex_dictionary_api.domain.TranslatorRepository

class GetTranslationFromAndroidUseCase(
    private val translatorRepository: com.example.yandex_dictionary_api.domain.TranslatorRepository
) {
    suspend fun invoke(model: TranslationModel): String? {
        return translatorRepository.getWordsFromAndroidTranslator(
            word = model.word,
            fromLang = model.inputLanguage,
            toLang = model.outputLanguage
        )
    }
}