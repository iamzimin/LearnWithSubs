package com.learnwithsubs.video_view.domain.usecase

import com.learnwithsubs.video_view.domain.models.TranslationModel
import com.learnwithsubs.video_view.domain.repository.TranslatorRepository

class GetTranslationFromAndroidUseCase(
    private val translatorRepository: TranslatorRepository
) {
    suspend fun invoke(model: TranslationModel): String? {
        return translatorRepository.getWordsFromAndroidTranslator(
            word = model.word,
            fromLang = model.inputLanguage,
            toLang = model.outputLanguage
        )
    }
}