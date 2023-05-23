package com.learnwithsubs.feature_video_view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.learnwithsubs.R

class VideoViewActivity : AppCompatActivity() {
    companion object {
        private const val STORAGE_PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_view)
        supportActionBar?.hide()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_REQUEST_CODE)
        else
            openVideo(intent.getStringExtra("videoURI"))

    }

    private fun openVideo(uri: String?) {
        videoView = findViewById(R.id.videoView)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        val videoUri = Uri.parse(uri)
        videoView.setVideoURI(videoUri)
        videoView.requestFocus()
        videoView.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    openVideo(intent.getStringExtra("videoURI"))
                else {
                    val videoIsUploading: String = applicationContext.getString(R.string.storage_access_required)
                    Toast.makeText(applicationContext, videoIsUploading, Toast.LENGTH_SHORT).show()
                    finish()
                }
                return
            }
        }
    }

}