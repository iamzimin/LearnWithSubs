package com.learnwithsubs.feature_video_list.data.repository

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.models.VideoStatus
import com.learnwithsubs.feature_video_list.domain.repository.VideoTranscodeRepository
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class VideoTranscodeRepositoryImpl(
    private val context: Context
) : VideoTranscodeRepository {
    private val videoProgressLiveData: MutableLiveData<Video?> = MutableLiveData()

    //val internalStorageDir = File(context.filesDir, "LearnWithSubs")
    private val externalStorageDir = File(Environment.getExternalStorageDirectory().toString() + "/Movies/", "LearnWithSubs")

    private var isVideoReady = false
    private var isAudioReady = false

    override suspend fun transcodeVideo(video: Video): Video? = suspendCoroutine { continuation ->
        if (!externalStorageDir.exists())
            externalStorageDir.mkdirs()

        val videoType = ".mp4"

        val outputVideoPath = File(externalStorageDir, "${video.id.toString()}$videoType")
        val command = "-i ${video.inputPath} -c:v mpeg4 -b:v ${video.bitrate}B $outputVideoPath -y"

        Config.enableStatisticsCallback { newStatistics ->
            video.uploadingProgress = ((newStatistics.time / video.duration.toDouble()) * 100).toInt()
            videoProgressLiveData.postValue(video)
        }

        FFmpeg.executeAsync(command) { executionId, returnCode ->
            when (returnCode) {
                RETURN_CODE_SUCCESS -> {
                    Log.i(Config.TAG, "Async command execution completed successfully.")
                    isVideoReady = true
                    if (isAudioReady) {
                        video.videoStatus = VideoStatus.NORMAL_VIDEO
                        isVideoReady = false
                        isAudioReady = false
                    }
                    video.outputPath = "${externalStorageDir.absolutePath}/${video.id}"
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

    override suspend fun extractAudio(video: Video): Video? = suspendCoroutine { continuation ->
        if (!externalStorageDir.exists())
            externalStorageDir.mkdirs()

        val audioType = ".mp3"
        val outputAudioPath = File(externalStorageDir, "${video.id.toString()}$audioType")
        val command = "-i ${video.inputPath} -q:a 0 -map a $outputAudioPath -y"

        Config.enableStatisticsCallback { newStatistics ->
            video.uploadingProgress = ((newStatistics.time / video.duration.toDouble()) * 100).toInt()
            videoProgressLiveData.postValue(video)
        }

        FFmpeg.executeAsync(command) { executionId, returnCode ->
            when (returnCode) {
                RETURN_CODE_SUCCESS -> {
                    Log.i(Config.TAG, "Async command execution completed successfully.")
                    isAudioReady = true
                    if (isVideoReady) {
                        video.videoStatus = VideoStatus.NORMAL_VIDEO
                        isVideoReady = false
                        isAudioReady = false
                    }
                    video.outputPath = "${externalStorageDir.absolutePath}/${video.id}"
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