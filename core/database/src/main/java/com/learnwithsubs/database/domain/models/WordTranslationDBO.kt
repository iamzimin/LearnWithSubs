package com.learnwithsubs.database.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.learnwithsubs.database.domain.Identifiable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class WordTranslationDBO(
    @PrimaryKey override val id: Int? = null,
    var word: String,
    var translation: String,
    val nativeLanguage: String,
    val learnLanguage: String,
    val timestamp: Long,
    val videoID: Int? = null,
    val videoName: String? = null,
): Parcelable, Identifiable