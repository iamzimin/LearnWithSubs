package com.learnwithsubs.feature_video_view.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.learnwithsubs.R

class VideoViewActivity : AppCompatActivity() {
    companion object {
        private const val STORAGE_PERMISSION_REQUEST_CODE = 1
    }

//    private val videoControls = findViewById<ConstraintLayout>(R.id.video_controls)
//
//    private val exitVideoView = findViewById<ImageButton>(R.id.exit_video_view)
//    private val videoName = findViewById<TextView>(R.id.video_name)
//    private val videoMenuButton = findViewById<ImageButton>(R.id.video_menu_button)
//
//    private val playVideoButton = findViewById<ImageButton>(R.id.play_video_button)
//    private val pauseVideoButton = findViewById<ImageButton>(R.id.pause_video_button)
//    private val forwardVideoButton = findViewById<ImageButton>(R.id.forward_5_video_button)
//    private val rewindVideoButton = findViewById<ImageButton>(R.id.rewind_5_video_button)
//
//    private val videoTime = findViewById<TextView>(R.id.video_time)
//    private val videoPlayStatus = findViewById<SeekBar>(R.id.video_play_status)

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
        val mediaController = CustomMediaController(this)
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