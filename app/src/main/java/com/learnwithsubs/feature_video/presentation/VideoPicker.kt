import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import com.learnwithsubs.feature_video.domain.models.Video
import com.learnwithsubs.feature_video.presentation.adapter.VideoAdapter
import com.learnwithsubs.feature_video.presentation.videos.VideoListViewModel
import com.learnwithsubs.feature_video.presentation.videos.VideosEvent
import java.util.Date


class VideoPicker(private val activity: Activity, private val requestCode: Int) {

    fun pickVideo() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type = "video/*"
        activity.startActivityForResult(intent, requestCode)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, vm: VideoListViewModel, context: Context) {
        if (requestCode == this.requestCode && resultCode == Activity.RESULT_OK) {
            val selectedVideoUri: Uri = data?.data ?: return
            val videoName: String = getVideoNameFromUri(
                videoUri = selectedVideoUri,
                context = context
            )
            val videoDuration = getVideoDuration(
                videoUri = selectedVideoUri,
                context = context
            )
            val currentTime = Date().time

            val video = Video(
                videoStatus = VideoAdapter.LOADING_VIDEO,
                name = videoName,
                preview = 0,
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
            val cursor: Cursor? = context.contentResolver.query(videoUri, projection, null, null, null)
            cursor.use { curs ->
                if (curs != null && curs.moveToFirst()) {
                    val displayNameIndex: Int = curs.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)
                    if (displayNameIndex != -1) {
                        displayName = curs.getString(displayNameIndex)
                    }
                }
            }
        }
        catch(_: Exception) { return "Video" }

        return displayName ?: "Video"
    }

    private fun getVideoDuration(videoUri: Uri, context: Context): Long {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, Uri.parse(videoUri.toString()))

        val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val duration = durationString?.toLongOrNull() ?: 0L

        retriever.release()

        return duration
    }
}
