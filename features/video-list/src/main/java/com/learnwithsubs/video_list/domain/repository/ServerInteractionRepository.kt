package com.learnwithsubs.video_list.domain.repository

import com.learnwithsubs.video_list.domain.models.Video
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ServerInteractionRepository {
    @Multipart
    @POST("/generate_subtitles")
    suspend fun sendAudioToServer(@Part audio: MultipartBody.Part): Response<String>
    suspend fun getSubtitles(video: Video): String?
}