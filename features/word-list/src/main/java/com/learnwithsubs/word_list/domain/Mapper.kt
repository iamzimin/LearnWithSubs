package com.learnwithsubs.word_list.domain

import com.learnwithsubs.database.domain.models.WordTranslationDBO
import com.learnwithsubs.word_list.domain.models.WordTranslation

internal fun WordTranslation.toWordTranslationDBO() : WordTranslationDBO {
    return WordTranslationDBO(
        id = this.id,
        word = this.word,
        translation = this.translation,
        nativeLanguage = this.nativeLanguage,
        learnLanguage = this.learnLanguage,
        timestamp = this.timestamp,
        videoID = this.videoID,
        videoName = this.videoName
    )
}

internal fun WordTranslationDBO.toWordTranslation() : WordTranslation {
    return WordTranslation(
        id = this.id,
        word = this.word,
        translation = this.translation,
        nativeLanguage = this.nativeLanguage,
        learnLanguage = this.learnLanguage,
        timestamp = this.timestamp,
        videoID = this.videoID,
        videoName = this.videoName
    )
}