package com.learnwithsubs.feature_word_list.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.learnwithsubs.general.models.Identifiable
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

@Parcelize
@Entity
data class WordTranslation(
    @PrimaryKey override val id: Int? = null,
    var word: String,
    var translation: String,
    val nativeLanguage: String,
    val learnLanguage: String,
    val timestamp: Long,
    val videoID: Int? = null,
    val videoName: String? = null,
): Parcelable, Identifiable