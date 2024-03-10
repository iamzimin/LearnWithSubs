package com.learnwithsubs.video_list.data.repository

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegKitConfig
import com.arthenica.ffmpegkit.FFmpegSession
import com.arthenica.ffmpegkit.FFmpegSessionCompleteCallback
import com.learnwithsubs.video_list.domain.VideoConstants
import com.learnwithsubs.video_list.domain.models.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class VideoTranscodeRepositoryImpl :
    com.learnwithsubs.video_list.domain.repository.VideoTranscodeRepository {
    private val videoProgressLiveData: MutableLiveData<com.learnwithsubs.video_list.domain.models.Video?> = MutableLiveData()
    private lateinit var transcodeVideoExecutionId: FFmpegSession
    private lateinit var extractAudioExecutionId: FFmpegSession

    //val internalStorageDir = File(context.filesDir, "LearnWithSubs")
    private val externalStorageDir = File(Environment.getExternalStorageDirectory().toString(), "LearnWithSubs")

    override suspend fun transcodeVideo(video: com.learnwithsubs.video_list.domain.models.Video): com.learnwithsubs.video_list.domain.models.Video? = suspendCoroutine { continuation ->
        val videoFolder = getVideoFolderPath(video)

        val outputVideoPath = File(videoFolder, com.learnwithsubs.video_list.domain.VideoConstants.COPIED_VIDEO)
        val command = "-i ${video.inputPath} -c:v mpeg4 -b:v ${video.bitrate}B ${outputVideoPath.absolutePath} -y"

        FFmpegKitConfig.enableStatisticsCallback {
            video.uploadingProgress = ((it.time / video.duration.toDouble()) * 100).toInt()
            if (video.uploadingProgress > 100) video.uploadingProgress = 0
            videoProgressLiveData.postValue(video)
        }

        transcodeVideoExecutionId = FFmpegKit.executeAsync(command, object : FFmpegSessionCompleteCallback {
            override fun apply(session: FFmpegSession?) {
                if (session != null) {
                   if (session.returnCode.isValueSuccess) {
                       Log.i("FFmpeg", "Async command execution completed successfully.")
                       video.uploadingProgress = 0
                       video.outputPath = videoFolder.absolutePath
                       continuation.resume(video)
                   } else if (session.returnCode.isValueCancel) {
                       Log.i("FFmpeg", "Async command execution cancelled by user.")
                       continuation.resume(null)
                   } else {
                       Log.i("FFmpeg", "Async command execution failed with returnCode=${session.returnCode}.")
                       video.errorType = com.learnwithsubs.video_list.domain.models.VideoErrorType.DECODING_VIDEO
                       continuation.resume(video)
                   }
                }
            }
        })

    }

    override fun cancelTranscodeVideo() {
        if (::transcodeVideoExecutionId.isInitialized)
            FFmpegKit.cancel(transcodeVideoExecutionId.sessionId)
    }

    override suspend fun extractAudio(video: com.learnwithsubs.video_list.domain.models.Video): com.learnwithsubs.video_list.domain.models.Video? = suspendCoroutine { continuation ->
        val videoFolder = getVideoFolderPath(video)

        val outputAudioPath = File(videoFolder, com.learnwithsubs.video_list.domain.VideoConstants.EXTRACTED_AUDIO)
        val command = "-i ${video.inputPath} -q:a 0 -map a ${outputAudioPath.absolutePath} -y"

        FFmpegKitConfig.enableStatisticsCallback {
            video.uploadingProgress = ((it.time / video.duration.toDouble()) * 100).toInt()
            if (video.uploadingProgress > 100) video.uploadingProgress = 0
            videoProgressLiveData.postValue(video)
        }

        extractAudioExecutionId = FFmpegKit.executeAsync(command, object : FFmpegSessionCompleteCallback {
            override fun apply(session: FFmpegSession?) {
                if (session != null) {
                    if (session.returnCode.isValueSuccess) {
                        Log.i("FFmpeg", "Async command execution completed successfully.")
                        video.uploadingProgress = 0
                        video.outputPath = videoFolder.absolutePath
                        continuation.resume(video)
                    } else if (session.returnCode.isValueCancel) {
                        Log.i("FFmpeg", "Async command execution cancelled by user.")
                        continuation.resume(null)
                    } else {
                        Log.i("FFmpeg", "Async command execution failed with returnCode=${session.returnCode}.")
                        video.errorType = com.learnwithsubs.video_list.domain.models.VideoErrorType.DECODING_VIDEO
                        continuation.resume(video)
                    }
                }
            }
        })
    }

    override fun cancelExtractAudio() {
        if (::extractAudioExecutionId.isInitialized)
            FFmpegKit.cancel(extractAudioExecutionId.sessionId)
    }

    override fun getVideoProgressLiveData(): MutableLiveData<com.learnwithsubs.video_list.domain.models.Video?> {
        return videoProgressLiveData
    }

    override suspend fun extractPreview(video: com.learnwithsubs.video_list.domain.models.Video) {
        val videoFolder = getVideoFolderPath(video)

        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(video.inputPath)
            val frame = retriever.getFrameAtTime(0)
            retriever.release()

            val outputVideoPath = File(videoFolder, com.learnwithsubs.video_list.domain.VideoConstants.VIDEO_PREVIEW)

            withContext(Dispatchers.IO) {
                FileOutputStream(outputVideoPath).use { out ->
                    frame?.compress(Bitmap.CompressFormat.JPEG, 100, out)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getVideoFolderPath(video: com.learnwithsubs.video_list.domain.models.Video): File {
        val videoFolder = File(externalStorageDir, video.id.toString())

        if (!videoFolder.exists())
            videoFolder.mkdirs()

        return videoFolder
    }
}