package com.learnwithsubs.feature_video_list.domain.usecase

import android.content.Context
import android.util.Log
import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.repository.ServerInteractionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File


class SendAudioToServerUseCase(
    private val serverInteractionRepository: ServerInteractionRepository
) {
    suspend fun invoke(video: Video?) {
        if (video != null) { // TODO
            val file =  File("${video.outputPath}.mp3")
            val requestFile = file.asRequestBody("audio/mp3".toMediaType())
            val audioPart = MultipartBody.Part.createFormData("audio", file.name, requestFile)

            try {
                val response = serverInteractionRepository.sendAudioToServer(audioPart)
                if (response.isSuccessful) {
                    handleSubtitlesResponse(video, response.body())
                } else { // TODO
                    val test = 0
                }
            } catch (e: Exception) { // TODO
                val test = 0
            }
        }
    }

    private fun handleSubtitlesResponse(video: Video, response: String?) {
        if (response == null) { // TODO
        }
        try {
            val subSTR = File("${video.outputPath}.srt")
            if (subSTR.exists())
                subSTR.delete()
            subSTR.createNewFile()
            val writer = subSTR.bufferedWriter()
            writer.write(response)
            writer.close()
        }catch (e: Exception) {
            // TODO edit toast
            //Toast.makeText(context.applicationContext, "Write error = $e.", Toast.LENGTH_SHORT).show()
        }
    }
}