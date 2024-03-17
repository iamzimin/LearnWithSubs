package com.example.server_api.domain.repository

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

interface ServerInteractionRepository {
    @Multipart
    @POST("/generate_subtitles")
    suspend fun sendAudioToServer(@Part audio: MultipartBody.Part): Response<String>
    suspend fun getSubtitles(videoFile: File): String?
}