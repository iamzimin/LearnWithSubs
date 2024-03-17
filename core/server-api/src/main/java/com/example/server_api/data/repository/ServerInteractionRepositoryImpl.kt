package com.example.server_api.data.repository

import com.example.server_api.domain.repository.ServerInteractionRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.lang.Exception


class ServerInteractionRepositoryImpl(
    private val retrofit: Retrofit
): ServerInteractionRepository {
    override suspend fun sendAudioToServer(audio: MultipartBody.Part): Response<String> { //TODO merge getSubtitles
        val apiService = retrofit.create(ServerInteractionRepository::class.java)
        return apiService.sendAudioToServer(audio)
    }

    override suspend fun getSubtitles(videoFile: File): String? {
        //val file = File(video.outputPath, VideoConstants.EXTRACTED_AUDIO)
        val requestFile = videoFile.asRequestBody("audio/mp3".toMediaType())
        val audioPart = MultipartBody.Part.createFormData("audio", videoFile.name, requestFile)

        return try {
            val response = sendAudioToServer(audioPart)
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