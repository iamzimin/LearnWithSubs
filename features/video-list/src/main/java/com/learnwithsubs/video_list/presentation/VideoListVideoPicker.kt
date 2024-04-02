package com.learnwithsubs.video_list.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import com.arthenica.ffmpegkit.FFprobeKit
import com.learnwithsubs.video_list.R
//import com.arthenica.mobileffmpeg.FFprobe
import com.learnwithsubs.video_list.domain.models.Video
import com.learnwithsubs.video_list.domain.models.VideoErrorType
import com.learnwithsubs.video_list.domain.models.VideoLoadingType
import com.learnwithsubs.video_list.domain.models.VideoStatus
import com.learnwithsubs.video_list.presentation.videos.VideoListViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.Date


class VideoListVideoPicker(private val fragment: Fragment, private val requestCode: Int) {

    fun pickVideo() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "video/*"
        fragment.startActivityForResult(intent, requestCode)
    }

    fun loadVideoOnResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        vm: VideoListViewModel,
        context: Context
    ) {
        if (requestCode == this.requestCode && resultCode == Activity.RESULT_OK) {
            val selectedVideoUri: Uri = data?.data ?: return
            var errorType: VideoErrorType? = null

            var path: String? = getVideoPath(context = context, videoUri = selectedVideoUri)
            val videoName: String = getVideoNameFromUri(selectedVideoUri, context)
            var videoDuration: Long? = getVideoDuration(selectedVideoUri, context)
            val currentTime = Date().time

            if (path == null || videoDuration == null) {
                path = ""
                videoDuration = 0
                errorType = VideoErrorType.PROCESSING_VIDEO
            }

            val video = Video(
                videoStatus = VideoStatus.LOADING_VIDEO,
                loadingType = VideoLoadingType.WAITING,
                errorType = errorType,
                name = videoName,
                inputPath = path,
                duration = videoDuration,
                URI = selectedVideoUri.toString(),
                timestamp = currentTime
            )

            vm.addVideo(video = video)
        }
    }

    private fun getVideoNameFromUri(videoUri: Uri, context: Context): String {
        val projection = arrayOf(OpenableColumns.DISPLAY_NAME)
        var displayName: String? = null
        try {
            val cursor: Cursor? =
                context.contentResolver.query(videoUri, projection, null, null, null)
            cursor.use { curs ->
                if (curs != null && curs.moveToFirst()) {
                    val displayNameIndex: Int =
                        curs.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)
                    if (displayNameIndex != -1) {
                        displayName = curs.getString(displayNameIndex)
                    }
                }
            }
        }
        catch (_: Exception) { return context.getString(R.string.video) }
        
        return displayName ?: context.getString(R.string.video)
    }

    private fun getVideoPath(context: Context, videoUri: Uri): String? {
        val contentResolver = context.contentResolver ?: return null

        val filePath =
            (context.applicationInfo.dataDir + File.separator + System.currentTimeMillis())
        val file = File(filePath)
        try {
            val inputStream = contentResolver.openInputStream(videoUri)
            val outputStream: OutputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            if (inputStream != null) {
                while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
            }
            outputStream.close()
            inputStream?.close()
        }
        catch (ignore: IOException) {
            return null
        }
        return file.absolutePath
    }

    private fun getVideoDuration(videoUri: Uri, context: Context): Long? {
        val retriever = MediaMetadataRetriever()
        val duration: Long?
        try {
            retriever.setDataSource(context, Uri.parse(videoUri.toString()))

            val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            duration = durationString?.toLongOrNull() ?: 0L

            retriever.release()
        } catch (e: Exception) {
            return null
        }

        return duration
    }
}
