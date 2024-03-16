package com.learnwithsubs.video_view.domain.usecase

import com.learnwithsubs.video_view.domain.repository.VideoViewRepository
import com.learnwithsubs.word_list.domain.models.WordTranslation

class SaveWordUseCase(
    private val videoViewRepository: VideoViewRepository
) {
    suspend fun invoke(word: WordTranslation) {
        videoViewRepository.saveWord(word = word)
    }
}