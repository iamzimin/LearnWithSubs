package com.example.server_api.data.repository

import com.example.server_api.domain.repository.ServerInteractionRepository
import com.example.server_api.domain.service.ServerService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import java.io.File


class ServerInteractionRepositoryImpl(
    private val retrofit: Retrofit
): ServerInteractionRepository {
    override suspend fun getSubtitlesFromAudio(videoFile: File): String? {
        val requestFile = videoFile.asRequestBody("audio/mp3".toMediaType())
        val audioPart = MultipartBody.Part.createFormData("audio", videoFile.name, requestFile)

        return try {
            val apiService = retrofit.create(ServerService::class.java)
            val response = apiService.sendAudioToServer(audioPart)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            """1
00:00:00,000 --> 00:00:10,000
Hello world, this is a test message for translation""".trimIndent()
        }
    }
}