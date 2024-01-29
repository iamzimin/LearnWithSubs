package com.learnwithsubs.feature_video_list.repository

import com.learnwithsubs.feature_video_list.models.Video
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