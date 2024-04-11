package com.learnwithsubs.video_view.domain.models

import com.example.base.Identifiable

data class Video(
    override val id: Int? = null,
    var isOwnSubtitles: Boolean = false,
    var name: String,
    val duration: Long,
    var watchProgress: Int = 0,
    var saveWords: Int = 0,
    var outputPath: String = "",
) : Identifiable
