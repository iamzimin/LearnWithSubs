package com.learnwithsubs.feature_video_list.repository

import com.learnwithsubs.feature_video_list.repository.ServerInteractionRepository
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.Retrofit


class ServerInteractionRepositoryImpl(
    private val retrofit: Retrofit
): ServerInteractionRepository {

    override suspend fun sendAudioToServer(audio: MultipartBody.Part): Response<String> {
        val apiService = retrofit.create(ServerInteractionRepository::class.java)
        return apiService.sendAudioToServer(audio)
    }

    /*
    override suspend fun convertAudioToByteArray(audioPath: String): ByteArray {

        val outputAudioFile = File(audioPath)
        val inputStream = outputAudioFile.inputStream()
        val outputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while (withContext(Dispatchers.IO) {
                inputStream.read(buffer)
            }.also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }
        val audioBytes = outputStream.toByteArray()

        withContext(Dispatchers.IO) {
            inputStream.close()
        }
        withContext(Dispatchers.IO) {
            outputStream.close()
        }
        return audioBytes
    }
     */
}