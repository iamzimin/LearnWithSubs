package com.learnwithsubs.video_view.domain.usecase

import com.learnwithsubs.server_translator_api.domain.repository.ServerTranslatorRepository
import com.learnwithsubs.video_view.domain.models.TranslationModel

class GetTranslationFromServerUseCase(
    private val serverTranslatorRepository: ServerTranslatorRepository
) {
    suspend fun invoke(model: TranslationModel): String? {
        return serverTranslatorRepository.getWordsFromServerTranslator(
            word = model.word,
            fromLang = model.inputLanguage,
            toLang = model.outputLanguage
        )
    }
}