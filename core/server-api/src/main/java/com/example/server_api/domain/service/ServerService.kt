package com.example.server_api.domain.service

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ServerService {
    @Multipart
    @POST("/generate_subtitles")
    suspend fun sendAudioToServer(
        @Part audio: MultipartBody.Part
    ): Response<String>
}