package com.learnwithsubs.feature_video_view.usecase

import com.learnwithsubs.feature_video_view.models.TranslationModel
import com.learnwithsubs.feature_video_view.repository.TranslatorRepository

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