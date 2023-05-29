package com.learnwithsubs.feature_video_list.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.models.VideoStatus
import com.learnwithsubs.feature_video_list.domain.repository.VideoTranscodeRepository
import com.learnwithsubs.feature_video_list.domain.usecase.LoadVideoUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class VideoTranscodeRepositoryImpl(
    private val context: Context
) : VideoTranscodeRepository {
    private val videoProgressLiveData: MutableLiveData<Video?> = MutableLiveData()

    override suspend fun transcodeVideo(video: Video): Video? = suspendCoroutine { continuation ->
        //Internal Storage
        val storageDir = File(context.filesDir, "LearnWithSubs")
        if (!storageDir.exists())
            storageDir.mkdirs()

        val bitrate = "2M"
        val videoType = ".mp4"

        val outputPath = File(storageDir, "${video.id.toString()}$videoType")
        val command = "-i ${video.inputPath} -c:v mpeg4 -b:v $bitrate $outputPath -y"

        /*
        External Storage!

        val storageDir = File(Environment.getExternalStorageDirectory().toString() + "/Movies/", "LearnWithSubs")
        if (!storageDir.exists())
            storageDir.mkdirs()

        video.outputPath = "$storageDir/${video.id}.mp4"

        val bitrate = "2M"
        val command = "-i ${video.inputPath} -c:v mpeg4 -b:v $bitrate ${video.outputPath} -y"
         */

        Config.enableStatisticsCallback { newStatistics ->
            video.uploadingProgress = ((newStatistics.time / video.duration.toDouble()) * 100).toInt()
            videoProgressLiveData.postValue(video)
        }

        FFmpeg.executeAsync(command) { executionId, returnCode ->
            when (returnCode) {
                RETURN_CODE_SUCCESS -> {
                    Log.i(Config.TAG, "Async command execution completed successfully.")
                    video.videoStatus = VideoStatus.NORMAL_VIDEO
                    video.outputPath = outputPath.absolutePath
                    continuation.resume(video)
                }
                RETURN_CODE_CANCEL -> {
                    Log.i(Config.TAG, "Async command execution cancelled by user.")
                    continuation.resume(null)
                }
                else -> {
                    Log.i(Config.TAG, "Async command execution failed with returnCode=$returnCode.")
                    Toast.makeText(
                        context.applicationContext,
                        "Async command execution failed with returnCode=$returnCode.",
                        Toast.LENGTH_SHORT
                    ).show() //TODO edit toast
                    continuation.resume(null)
                }
            }
        }

    }

    override fun getVideoProgressLiveData(): MutableLiveData<Video?> {
        return videoProgressLiveData
    }
}