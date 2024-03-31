package com.learnwithsubs.word_list.domain

import com.learnwithsubs.database.domain.models.WordTranslationDBO
import com.learnwithsubs.word_list.domain.models.WordTranslation

internal fun WordTranslation.toWordTranslationDBO() : WordTranslationDBO {
    TODO("Not Implemented")
}

internal fun WordTranslationDBO.toWordTranslation() : WordTranslation { //TODO
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