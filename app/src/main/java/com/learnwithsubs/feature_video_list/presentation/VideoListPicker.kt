import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import com.arthenica.mobileffmpeg.FFprobe
import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.models.VideoStatus
import com.learnwithsubs.feature_video_list.presentation.videos.VideoListViewModel
import com.learnwithsubs.feature_video_list.presentation.videos.VideosEvent
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.Date


class VideoListPicker(private val activity: Activity, private val requestCode: Int) {

    fun pickVideo() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type = "video/*"
        activity.startActivityForResult(intent, requestCode)
    }

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        vm: VideoListViewModel,
        context: Context
    ) {
        if (requestCode == this.requestCode && resultCode == Activity.RESULT_OK) {
            val selectedVideoUri: Uri = data?.data ?: return
            val path: String = getVideoPath(context = context, videoUri = selectedVideoUri) ?: return

            val videoName: String = getVideoNameFromUri(selectedVideoUri, context)
            val videoDuration = getVideoDuration(selectedVideoUri, context)
            val currentTime = Date().time

            val video = Video(
                videoStatus = VideoStatus.LOADING_VIDEO,
                name = videoName,
                preview = 0,
                inputPath = path,
                duration = videoDuration,
                URI = selectedVideoUri.toString(),
                timestamp = currentTime
            )
            vm.onEvent(event = VideosEvent.LoadVideo(video = video))
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
        catch (_: Exception) { return "Video" }
        
        return displayName ?: "Video"
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

    private fun getVideoDuration(videoUri: Uri, context: Context): Int {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, Uri.parse(videoUri.toString())) //TODO если файл сломан -> ошибка

        val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val duration = durationString?.toIntOrNull() ?: 0L

        retriever.release()

        return duration.toInt()
    }
}
