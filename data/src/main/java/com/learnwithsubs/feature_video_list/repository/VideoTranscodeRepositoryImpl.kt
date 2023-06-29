package com.learnwithsubs.feature_video_list.repository

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import com.learnwithsubs.feature_video_list.VideoConstants
import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.models.VideoErrorType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class VideoTranscodeRepositoryImpl : VideoTranscodeRepository {
    private val videoProgressLiveData: MutableLiveData<Video?> = MutableLiveData()

    //val internalStorageDir = File(context.filesDir, "LearnWithSubs")
    private val externalStorageDir = File(Environment.getExternalStorageDirectory().toString(), "LearnWithSubs")

    override suspend fun transcodeVideo(video: Video): Video? = suspendCoroutine { continuation ->
        val videoFolder = getVideoFolderPath(video)

        val outputVideoPath = File(videoFolder, VideoConstants.COPIED_VIDEO)
        val command = "-i ${video.inputPath} -c:v mpeg4 -b:v ${video.bitrate}B ${outputVideoPath.absolutePath} -y"

        Config.enableStatisticsCallback { newStatistics ->
            video.uploadingProgress = ((newStatistics.time / video.duration.toDouble()) * 100).toInt()
            if (video.uploadingProgress > 100) video.uploadingProgress = 0
            videoProgressLiveData.postValue(video)
        }

        FFmpeg.executeAsync(command) { executionId, returnCode ->
            when (returnCode) {
                RETURN_CODE_SUCCESS -> {
                    Log.i(Config.TAG, "Async command execution completed successfully.")
                    video.uploadingProgress = 0
                    video.outputPath = videoFolder.absolutePath
                    continuation.resume(video)
                }
                RETURN_CODE_CANCEL -> {
                    Log.i(Config.TAG, "Async command execution cancelled by user.")
                    continuation.resume(null)
                }
                else -> {
                    Log.i(Config.TAG, "Async command execution failed with returnCode=$returnCode.")
                    video.errorType = VideoErrorType.DECODING_VIDEO
                    continuation.resume(video)
                }
            }
        }

    }

    override suspend fun extractAudio(video: Video): Video? = suspendCoroutine { continuation ->
        val videoFolder = getVideoFolderPath(video)

        val outputAudioPath = File(videoFolder, VideoConstants.EXTRACTED_AUDIO)
        val command = "-i ${video.inputPath} -q:a 0 -map a ${outputAudioPath.absolutePath} -y"

        Config.enableStatisticsCallback { newStatistics ->
            video.uploadingProgress = ((newStatistics.time / video.duration.toDouble()) * 100).toInt()
            if (video.uploadingProgress > 100) video.uploadingProgress = 0
            videoProgressLiveData.postValue(video)
        }

        FFmpeg.executeAsync(command) { executionId, returnCode ->
            when (returnCode) {
                RETURN_CODE_SUCCESS -> {
                    Log.i(Config.TAG, "Async command execution completed successfully.")
                    video.uploadingProgress = 0
                    video.outputPath = videoFolder.absolutePath
                    continuation.resume(video)
                }
                RETURN_CODE_CANCEL -> {
                    Log.i(Config.TAG, "Async command execution cancelled by user.")
                    continuation.resume(null)
                }
                else -> {
                    Log.i(Config.TAG, "Async command execution failed with returnCode=$returnCode.")
                    video.errorType = VideoErrorType.EXTRACTING_AUDIO
                    continuation.resume(video)
                }
            }
        }
    }

    override fun getVideoProgressLiveData(): MutableLiveData<Video?> {
        return videoProgressLiveData
    }

    override suspend fun extractPreview(video: Video) {
        val videoFolder = getVideoFolderPath(video)

        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(video.inputPath)
        val frame = retriever.getFrameAtTime(0)
        retriever.release()

        val outputVideoPath = File(videoFolder, VideoConstants.VIDEO_PREVIEW)
        try { //TODO
            withContext(Dispatchers.IO) {
                FileOutputStream(outputVideoPath).use { out ->
                    frame?.compress(Bitmap.CompressFormat.JPEG, 100, out)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getVideoFolderPath(video: Video): File {
        val videoFolder = File(externalStorageDir, video.id.toString())

        if (!videoFolder.exists())
            videoFolder.mkdirs()

        return videoFolder
    }
}