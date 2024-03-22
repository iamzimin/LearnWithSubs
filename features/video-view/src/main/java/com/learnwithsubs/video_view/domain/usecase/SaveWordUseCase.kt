package com.learnwithsubs.video_view.domain.usecase

import com.learnwithsubs.video_view.domain.models.WordTranslation
import com.learnwithsubs.video_view.domain.repository.VideoViewRepository
import com.learnwithsubs.video_view.domain.toWordTranslationDBO

class SaveWordUseCase(
    private val videoViewRepository: VideoViewRepository
) {
    suspend fun invoke(word: WordTranslation) {
        videoViewRepository.saveWord(word = word.toWordTranslationDBO())
    }
}