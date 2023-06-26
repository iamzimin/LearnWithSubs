package com.learnwithsubs.feature_video_view.service

import com.learnwithsubs.feature_video_view.models.server.ServerTimeResponse
import retrofit2.Call
import retrofit2.http.GET

interface ServerTimeService {
    @GET("Time/current/zone?timeZone=Europe/Amsterdam")
    fun getCurrentTime(): Call<ServerTimeResponse>
}