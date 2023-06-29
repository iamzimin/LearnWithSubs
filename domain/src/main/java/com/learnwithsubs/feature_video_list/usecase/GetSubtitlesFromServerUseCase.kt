package com.learnwithsubs.feature_video_list.usecase

import com.learnwithsubs.feature_video_list.VideoConstants
import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.models.VideoErrorType
import com.learnwithsubs.feature_video_list.repository.ServerInteractionRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.lang.Exception


class GetSubtitlesFromServerUseCase(
    private val serverInteractionRepository: ServerInteractionRepository
) {
    suspend fun invoke(video: Video): Video {
        val file = File(video.outputPath, VideoConstants.EXTRACTED_AUDIO)
        val requestFile = file.asRequestBody("audio/mp3".toMediaType())
        val audioPart = MultipartBody.Part.createFormData("audio", file.name, requestFile)

        try {
            val response = serverInteractionRepository.sendAudioToServer(audioPart)
            if (response.isSuccessful) {
                val subResponse = response.body() ?: return video.apply { errorType = VideoErrorType.GENERATING_SUBTITLES }
                handleSubtitlesResponse(video, subResponse)
                return video
            } else {
                return video.apply { errorType = VideoErrorType.GENERATING_SUBTITLES }
            }
        } catch (e: Exception) {
//            // TEMP
//            handleSubtitlesResponse(video, """1
//00:00:00,000 --> 00:00:10,000
//Hello word""".trimIndent())
//            return video
            return video.apply { errorType = VideoErrorType.GENERATING_SUBTITLES }
        }


    }

    private fun handleSubtitlesResponse(video: Video, response: String) {
        val subSTR = File(video.outputPath, VideoConstants.GENERATED_SUBTITLES)
        if (subSTR.exists())
            subSTR.delete()
        subSTR.createNewFile()
        val writer = subSTR.bufferedWriter()
        writer.write(response)
        writer.close()
    }
}