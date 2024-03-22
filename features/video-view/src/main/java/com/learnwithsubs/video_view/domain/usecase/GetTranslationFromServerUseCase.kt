package com.learnwithsubs.video_view.domain.usecase

import com.example.yandex_dictionary_api.domain.repository.TranslatorRepository
import com.learnwithsubs.video_view.domain.models.TranslationModel

class GetTranslationFromServerUseCase(
    private val translatorRepository: TranslatorRepository
) {
    suspend fun invoke(model: TranslationModel): String? {
        return translatorRepository.getWordsFromServerTranslator(
            word = model.word,
            fromLang = model.inputLanguage,
            toLang = model.outputLanguage
        )
    }
}