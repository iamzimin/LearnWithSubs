package com.example.server_api.domain.repository

import java.io.File

interface ServerInteractionRepository {
    suspend fun getSubtitlesFromAudio(videoFile: File): String?
}