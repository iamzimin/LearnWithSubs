package com.learnwithsubs.video_list.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.learnwithsubs.resource.R
import com.learnwithsubs.video_list.domain.models.Video
import com.learnwithsubs.video_list.presentation.videos.VideoListViewModel

class VideoListSubtitlePicker(private val fragment: Fragment, private val requestCode: Int) {

    fun pickSubtitles() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        fragment.startActivityForResult(intent, requestCode)
    }

    fun loadVideoOnResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        vm: VideoListViewModel,
        video: Video?,
    ) {
        if (requestCode == this.requestCode && resultCode == Activity.RESULT_OK) {
            val selectedFileUri: Uri = data?.data ?: return
            try {
                val inputStream = fragment.context?.contentResolver?.openInputStream(selectedFileUri)
                val fileContent = inputStream?.bufferedReader().use { it?.readText() } ?: return
                video ?: return

                vm.loadNewSubtitles(video = video, subtitles = fileContent)

                inputStream?.close()
            } catch (e: Exception) {
                Toast.makeText(fragment.context, R.string.subtitle_extraction_error, Toast.LENGTH_SHORT).show()
            }
        }
    }


}
