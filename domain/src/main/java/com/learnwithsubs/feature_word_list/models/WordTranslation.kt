package com.learnwithsubs.feature_word_list.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.learnwithsubs.models.Identifiable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class WordTranslation(
    @PrimaryKey override val id: Int? = null,
    val word: String,
    val translation: String,
): Parcelable, Identifiable
