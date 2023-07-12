package com.learnwithsubs.feature_video_view.usecase

import com.learnwithsubs.feature_video_view.repository.VideoViewRepository
import com.learnwithsubs.feature_word_list.models.WordTranslation

class SaveWordUseCase(
    private val videoViewRepository: VideoViewRepository
) {
    suspend fun invoke(word: WordTranslation) {
        videoViewRepository.saveWord(word = word)
    }
}