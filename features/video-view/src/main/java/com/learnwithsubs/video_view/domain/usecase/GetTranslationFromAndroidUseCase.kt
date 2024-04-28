package com.learnwithsubs.video_view.domain.usecase

import com.learnwithsubs.android_translator.domain.repository.AndroidTranslatorRepository
import com.learnwithsubs.video_view.domain.models.TranslationModel

class GetTranslationFromAndroidUseCase(
    private val androidTranslatorRepository: AndroidTranslatorRepository
) {
    suspend fun invoke(model: TranslationModel): String? {
        return androidTranslatorRepository.getWordsFromAndroidTranslator(
            word = model.word,
            fromLang = model.inputLanguage,
            toLang = model.outputLanguage
        )
    }
}