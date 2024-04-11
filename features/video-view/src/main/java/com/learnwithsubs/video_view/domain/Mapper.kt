package com.learnwithsubs.video_view.domain

import com.example.yandex_dictionary_api.models.DictionaryTypeDTO
import com.example.yandex_dictionary_api.models.DictionaryWordDTO
import com.learnwithsubs.database.domain.models.VideoDBO
import com.learnwithsubs.database.domain.models.VideoErrorTypeDBO
import com.learnwithsubs.database.domain.models.VideoLoadingTypeDBO
import com.learnwithsubs.database.domain.models.VideoStatusDBO
import com.learnwithsubs.database.domain.models.WordTranslationDBO
import com.learnwithsubs.video_view.domain.models.DictionarySynonyms
import com.learnwithsubs.video_view.domain.models.DictionaryType
import com.learnwithsubs.video_view.domain.models.DictionaryWord
import com.learnwithsubs.video_view.domain.models.Video
import com.learnwithsubs.video_view.domain.models.WordTranslation

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

internal fun VideoDBO.toVideo() : Video {
    return Video(
        id = this.id,
        //videoStatus = this.videoStatus.toVideoStatus(),
        //loadingType = this.loadingType.toVideoLoadingType(),
        //errorType = this.errorType?.toVideoErrorType(),
        isOwnSubtitles = this.isOwnSubtitles,
        name = this.name,
        duration = this.duration,
        watchProgress = this.watchProgress,
        saveWords = this.saveWords,
        //uploadingProgress = this.uploadingProgress,
        //URI = this.URI,
        //inputPath = this.inputPath,
        outputPath = this.outputPath,
        //timestamp = this.timestamp
    )
}

internal fun DictionaryWordDTO.toDictionaryWord(): DictionaryWord {
    val mappedSynonyms = ArrayList<DictionarySynonyms>()
    for (synonymDTO in synonyms) {
        mappedSynonyms.add(
            DictionarySynonyms(
                id = synonymDTO.id,
                word = synonymDTO.word,
                translation = synonymDTO.translation,
                partSpeech = synonymDTO.partSpeech,
                type = synonymDTO.type.toDictionaryType()
            )
        )
    }
    return DictionaryWord(
        translation = translation,
        synonyms = mappedSynonyms
    )
}

internal fun DictionaryTypeDTO.toDictionaryType(): DictionaryType {
    return when (this) {
        DictionaryTypeDTO.WORD -> DictionaryType.WORD
        DictionaryTypeDTO.PART_SPEECH -> DictionaryType.PART_SPEECH
    }
}


/*
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

internal fun Video.toVideoDBO() : VideoDBO {
    return VideoDBO(
        id = this.id,
        videoStatus = this.videoStatus.toVideoStatusDBO(),
        loadingType = this.loadingType.toVideoLoadingTypeDBO(),
        errorType = this.errorType?.toVideoErrorTypeDBO(),
        isOwnSubtitles = this.isOwnSubtitles,
        name = this.name,
        duration = this.duration,
        watchProgress = this.watchProgress,
        saveWords = this.saveWords,
        uploadingProgress = this.uploadingProgress,
        URI = this.URI,
        inputPath = this.inputPath,
        outputPath = this.outputPath,
        timestamp = this.timestamp
    )
}


internal fun VideoStatus.toVideoStatusDBO(): VideoStatusDBO {
    return when (this) {
        VideoStatus.NORMAL_VIDEO -> VideoStatusDBO.NORMAL_VIDEO
        VideoStatus.LOADING_VIDEO -> VideoStatusDBO.LOADING_VIDEO
    }
}
internal fun VideoLoadingType.toVideoLoadingTypeDBO(): VideoLoadingTypeDBO {
    return when (this) {
        VideoLoadingType.WAITING -> VideoLoadingTypeDBO.WAITING
        VideoLoadingType.EXTRACTING_AUDIO -> VideoLoadingTypeDBO.EXTRACTING_AUDIO
        VideoLoadingType.DECODING_VIDEO -> VideoLoadingTypeDBO.DECODING_VIDEO
        VideoLoadingType.LOADING_AUDIO -> VideoLoadingTypeDBO.LOADING_AUDIO
        VideoLoadingType.GENERATING_SUBTITLES -> VideoLoadingTypeDBO.GENERATING_SUBTITLES
        VideoLoadingType.DONE -> VideoLoadingTypeDBO.DONE
    }
}
internal fun VideoErrorType.toVideoErrorTypeDBO(): VideoErrorTypeDBO {
    return when (this) {
        VideoErrorType.PROCESSING_VIDEO -> VideoErrorTypeDBO.PROCESSING_VIDEO
        VideoErrorType.EXTRACTING_AUDIO -> VideoErrorTypeDBO.EXTRACTING_AUDIO
        VideoErrorType.DECODING_VIDEO -> VideoErrorTypeDBO.DECODING_VIDEO
        VideoErrorType.UPLOADING_AUDIO -> VideoErrorTypeDBO.UPLOADING_AUDIO
        VideoErrorType.GENERATING_SUBTITLES -> VideoErrorTypeDBO.GENERATING_SUBTITLES
    }
}




internal fun VideoStatusDBO.toVideoStatus(): VideoStatus {
    return when (this) {
        VideoStatusDBO.NORMAL_VIDEO -> VideoStatus.NORMAL_VIDEO
        VideoStatusDBO.LOADING_VIDEO -> VideoStatus.LOADING_VIDEO
    }
}
internal fun VideoLoadingTypeDBO.toVideoLoadingType(): VideoLoadingType {
    return when (this) {
        VideoLoadingTypeDBO.WAITING -> VideoLoadingType.WAITING
        VideoLoadingTypeDBO.EXTRACTING_AUDIO -> VideoLoadingType.EXTRACTING_AUDIO
        VideoLoadingTypeDBO.DECODING_VIDEO -> VideoLoadingType.DECODING_VIDEO
        VideoLoadingTypeDBO.LOADING_AUDIO -> VideoLoadingType.LOADING_AUDIO
        VideoLoadingTypeDBO.GENERATING_SUBTITLES -> VideoLoadingType.GENERATING_SUBTITLES
        VideoLoadingTypeDBO.DONE -> VideoLoadingType.DONE
    }
}
internal fun VideoErrorTypeDBO.toVideoErrorType(): VideoErrorType {
    return when (this) {
        VideoErrorTypeDBO.PROCESSING_VIDEO -> VideoErrorType.PROCESSING_VIDEO
        VideoErrorTypeDBO.EXTRACTING_AUDIO -> VideoErrorType.EXTRACTING_AUDIO
        VideoErrorTypeDBO.DECODING_VIDEO -> VideoErrorType.DECODING_VIDEO
        VideoErrorTypeDBO.UPLOADING_AUDIO -> VideoErrorType.UPLOADING_AUDIO
        VideoErrorTypeDBO.GENERATING_SUBTITLES -> VideoErrorType.GENERATING_SUBTITLES
    }
}
*/