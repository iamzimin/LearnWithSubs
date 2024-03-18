package com.example.video_transcode.data.repository

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegKitConfig
import com.arthenica.ffmpegkit.FFmpegSession
import com.arthenica.ffmpegkit.FFmpegSessionCompleteCallback
import com.example.video_transcode.domain.VideoConstants
import com.example.video_transcode.domain.models.VideoTranscode
import com.example.video_transcode.domain.models.VideoErrorType
import com.example.video_transcode.domain.repository.VideoTranscodeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class VideoTranscodeRepositoryImpl : VideoTranscodeRepository {
    private val videoTranscodeProgressLiveData: MutableLiveData<VideoTranscode?> = MutableLiveData()
    private lateinit var transcodeVideoExecutionId: FFmpegSession
    private lateinit var extractAudioExecutionId: FFmpegSession

    //val internalStorageDir = File(context.filesDir, "LearnWithSubs")
    private val externalStorageDir = File(Environment.getExternalStorageDirectory().toString(), "LearnWithSubs")

    override suspend fun transcodeVideo(videoTranscode: VideoTranscode): VideoTranscode? = suspendCoroutine { continuation ->
        val videoFolder = getVideoFolderPath(videoTranscode)

        val outputVideoPath = File(videoFolder, VideoConstants.COPIED_VIDEO)
        val command = "-i ${videoTranscode.inputPath} -c:v mpeg4 -b:v ${videoTranscode.bitrate}B ${outputVideoPath.absolutePath} -y"

        FFmpegKitConfig.enableStatisticsCallback {
            videoTranscode.uploadingProgress = ((it.time / videoTranscode.duration.toDouble()) * 100).toInt()
            if (videoTranscode.uploadingProgress > 100) videoTranscode.uploadingProgress = 0
            videoTranscodeProgressLiveData.postValue(videoTranscode)
        }

        transcodeVideoExecutionId = FFmpegKit.executeAsync(command, object : FFmpegSessionCompleteCallback {
            override fun apply(session: FFmpegSession?) {
                if (session != null) {
                   if (session.returnCode.isValueSuccess) {
                       Log.i("FFmpeg", "Async command execution completed successfully.")
                       videoTranscode.uploadingProgress = 0
                       videoTranscode.outputPath = videoFolder.absolutePath
                       continuation.resume(videoTranscode)
                   } else if (session.returnCode.isValueCancel) {
                       Log.i("FFmpeg", "Async command execution cancelled by user.")
                       continuation.resume(null)
                   } else {
                       Log.i("FFmpeg", "Async command execution failed with returnCode=${session.returnCode}.")
                       videoTranscode.errorType = VideoErrorType.DECODING_VIDEO
                       continuation.resume(videoTranscode)
                   }
                }
            }
        })

    }

    override fun cancelTranscodeVideo() {
        if (::transcodeVideoExecutionId.isInitialized)
            FFmpegKit.cancel(transcodeVideoExecutionId.sessionId)
    }

    override suspend fun extractAudio(videoTranscode: VideoTranscode): VideoTranscode? = suspendCoroutine { continuation ->
        val videoFolder = getVideoFolderPath(videoTranscode)

        val outputAudioPath = File(videoFolder, VideoConstants.EXTRACTED_AUDIO)
        val command = "-i ${videoTranscode.inputPath} -q:a 0 -map a ${outputAudioPath.absolutePath} -y"

        FFmpegKitConfig.enableStatisticsCallback {
            videoTranscode.uploadingProgress = ((it.time / videoTranscode.duration.toDouble()) * 100).toInt()
            if (videoTranscode.uploadingProgress > 100) videoTranscode.uploadingProgress = 0
            videoTranscodeProgressLiveData.postValue(videoTranscode)
        }

        extractAudioExecutionId = FFmpegKit.executeAsync(command, object : FFmpegSessionCompleteCallback {
            override fun apply(session: FFmpegSession?) {
                if (session != null) {
                    if (session.returnCode.isValueSuccess) {
                        Log.i("FFmpeg", "Async command execution completed successfully.")
                        videoTranscode.uploadingProgress = 0
                        videoTranscode.outputPath = videoFolder.absolutePath
                        continuation.resume(videoTranscode)
                    } else if (session.returnCode.isValueCancel) {
                        Log.i("FFmpeg", "Async command execution cancelled by user.")
                        continuation.resume(null)
                    } else {
                        Log.i("FFmpeg", "Async command execution failed with returnCode=${session.returnCode}.")
                        videoTranscode.errorType = VideoErrorType.DECODING_VIDEO
                        continuation.resume(videoTranscode)
                    }
                }
            }
        })
    }

    override fun cancelExtractAudio() {
        if (::extractAudioExecutionId.isInitialized)
            FFmpegKit.cancel(extractAudioExecutionId.sessionId)
    }

    override fun getVideoProgressLiveData(): MutableLiveData<VideoTranscode?> {
        return videoTranscodeProgressLiveData
    }

    override suspend fun extractPreview(videoTranscode: VideoTranscode) {
        val videoFolder = getVideoFolderPath(videoTranscode)

        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(videoTranscode.inputPath)
            val frame = retriever.getFrameAtTime(0)
            retriever.release()

            val outputVideoPath = File(videoFolder, VideoConstants.VIDEO_PREVIEW)

            withContext(Dispatchers.IO) {
                FileOutputStream(outputVideoPath).use { out ->
                    frame?.compress(Bitmap.CompressFormat.JPEG, 100, out)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getVideoFolderPath(videoTranscode: VideoTranscode): File {
        val videoFolder = File(externalStorageDir, videoTranscode.id.toString())

        if (!videoFolder.exists())
            videoFolder.mkdirs()

        return videoFolder
    }
}