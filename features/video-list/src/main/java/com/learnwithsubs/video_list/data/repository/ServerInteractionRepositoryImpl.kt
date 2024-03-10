package com.learnwithsubs.video_list.data.repository

import com.learnwithsubs.database.domain.models.Video
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.lang.Exception


class ServerInteractionRepositoryImpl(
    private val retrofit: Retrofit
): com.learnwithsubs.video_list.domain.repository.ServerInteractionRepository {
    override suspend fun sendAudioToServer(audio: MultipartBody.Part): Response<String> { //TODO merge getSubtitles
        val apiService = retrofit.create(com.learnwithsubs.video_list.domain.repository.ServerInteractionRepository::class.java)
        return apiService.sendAudioToServer(audio)
    }

    override suspend fun getSubtitles(video: com.learnwithsubs.database.domain.models.Video): String? {
        val file = File(video.outputPath, com.learnwithsubs.video_list.domain.VideoConstants.EXTRACTED_AUDIO)
        val requestFile = file.asRequestBody("audio/mp3".toMediaType())
        val audioPart = MultipartBody.Part.createFormData("audio", file.name, requestFile)

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