package com.learnwithsubs.feature_video_list.domain.usecase

import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.repository.ServerInteractionRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class SendAudioToServerUseCase(
    private val serverInteractionRepository: ServerInteractionRepository
) {
    suspend fun invoke(video: Video?) {
        if (video != null) { // TODO
            val file =  File("${video.outputPath}.mp3")
            val requestBody = file.asRequestBody("audio/*".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData("audio", file.absolutePath, requestBody)


            //try {
                val response = serverInteractionRepository.sendAudioToServer()
                if (response.isSuccessful) {
                    val test = 0
                    val test2 = 0
                    handleSubtitlesResponse(video, response.message())
                } else {
                    val test = 0
                    val test2 = 0
                }
//            } catch (e: Exception) {
//                val test = 0
//                val test2 = 0
//            }
        }
    }

    private fun handleSubtitlesResponse(video: Video, response: String) {
        val responseReplaced = response.replace("<br>", "\n")
        try {
            val subSTR = File("${video.outputPath}.srt")
            if (subSTR.exists())
                subSTR.delete()
            subSTR.createNewFile()
            val writer = subSTR.bufferedWriter()
            writer.write(responseReplaced)
            writer.close()
        }catch (e: Exception) {
            // TODO edit toast
            //Toast.makeText(context.applicationContext, "Write error = $e.", Toast.LENGTH_SHORT).show()
        }
    }
}