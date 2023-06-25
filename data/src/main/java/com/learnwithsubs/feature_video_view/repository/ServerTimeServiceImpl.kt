package com.learnwithsubs.feature_video_view.repository

import com.learnwithsubs.feature_video_view.models.server.ServerTimeResponse
import retrofit2.Call
import retrofit2.Retrofit

class ServerTimeServiceImpl(
    private val serverTime: Retrofit
): ServerTimeService {
    override fun getCurrentTime(): Call<ServerTimeResponse> {
        val apiService = serverTime.create(ServerTimeService::class.java)
        return apiService.getCurrentTime()
    }
}