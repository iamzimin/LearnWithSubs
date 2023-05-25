package com.learnwithsubs.feature_video_view.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.learnwithsubs.R
import com.learnwithsubs.app.App
import com.learnwithsubs.feature_video_view.presentation.videos.VideoViewViewModel
import com.learnwithsubs.feature_video_view.presentation.videos.VideoViewViewModelFactory
import javax.inject.Inject


class VideoViewActivity : AppCompatActivity() {
    companion object {
        private const val STORAGE_PERMISSION_REQUEST_CODE = 1
    }

    @Inject
    lateinit var vmFactory: VideoViewViewModelFactory
    private lateinit var vm: VideoViewViewModel

    private lateinit var videoView: VideoView
    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_view)
        supportActionBar?.hide()


        // Get view by id
        videoView = findViewById(R.id.videoView)
        val videoControls = findViewById<ConstraintLayout>(R.id.video_controls)

        val exitVideoView = findViewById<ImageButton>(R.id.exit_video_view)
        val videoName = findViewById<TextView>(R.id.video_name)
        val videoMenuButton = findViewById<ImageButton>(R.id.video_menu_button)

        val playVideoButton = findViewById<ImageButton>(R.id.play_video_button)
        val pauseVideoButton = findViewById<ImageButton>(R.id.pause_video_button)
        val forwardVideoButton = findViewById<ImageButton>(R.id.forward_5_video_button)
        val rewindVideoButton = findViewById<ImageButton>(R.id.rewind_5_video_button)

        val videoTime = findViewById<TextView>(R.id.video_time)
        val videoPlaySeekBar = findViewById<SeekBar>(R.id.video_play_status)





        // Set VM
        (applicationContext as App).videoViewAppComponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)[VideoViewViewModel::class.java]
        vm.currentVideo.value = intent.getParcelableExtra("videoData")

        // Set VideoView
        val mediaController = CustomMediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        //Video Play
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_MEDIA_VIDEO), STORAGE_PERMISSION_REQUEST_CODE)
        else
            vm.currentVideo.value?.let { vm.openVideo(video = it) }





        // Live Data - Click Listener

        // URI listener
        vm.videoPath.observe(this) { uri ->
            val videoUriParse = Uri.parse(uri)
            videoView.setVideoURI(videoUriParse)
            videoView.requestFocus()
            videoView.start()
            val watchProgress = vm.currentVideo.value?.watchProgress ?: 0
            videoView.seekTo(watchProgress)
        }


        // Video name
        vm.videoName.observe(this) { name ->
            videoName.text = name
        }


        // Video time update
        val handler = Handler(Looper.getMainLooper()) // TODO может не правильно в mvvm (возможда онибка с некорректным отображением времени)
        handler.post(object : Runnable {
            override fun run() {
                vm.updateCurrentTime(videoView.currentPosition)
                handler.postDelayed(this, 1000)
            }
        })
        vm.videoTime.observe(this) { time ->
            videoTime.text = time
        }


        // Seek bar
        vm.videoSeekBarProgress.observe(this) { progress ->
            videoPlaySeekBar.progress = progress
        }
        videoPlaySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val newPosition = (videoView.duration * progress) / 100
                    videoView.seekTo(newPosition)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


        // Button forward/
        videoView.setOnPreparedListener {vid ->
            forwardVideoButton.setOnClickListener {
                val new = vid.currentPosition + 5000
                vid.seekTo(new)
            }
            rewindVideoButton.setOnClickListener {
                val new = vid.currentPosition - 5000
                vid.seekTo(new)
            }
        }


        // Button show
        videoView.setOnClickListener {
            vm.isButtonsShowed.value = vm.isButtonsShowed.value != true
        }
        vm.isButtonsShowed.observe(this) { isButtonsShowed ->
            videoControls.visibility = if (isButtonsShowed) View.VISIBLE else View.INVISIBLE
        }


        // Video play/pause
        pauseVideoButton.setOnClickListener {
            vm.videoPlaying.value = vm.videoPlaying.value != true
        }
        playVideoButton.setOnClickListener {
            vm.videoPlaying.value = vm.videoPlaying.value != true
        }
        vm.videoPlaying.observe(this) { isPlaying ->
            videoView.apply {
                if (isPlaying) start() else pause()
            }
            playVideoButton.apply {
                visibility = if (isPlaying) View.INVISIBLE else View.VISIBLE
            }
            pauseVideoButton.apply {
                visibility = if (!isPlaying) View.INVISIBLE else View.VISIBLE
            }
        }


        // Exit
        exitVideoView.setOnClickListener {
            finish()
        }
    }


    override fun onPause() {
        vm.currentVideo.value?.let { vm.saveVideo(it) }
        super.onPause()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        currentPosition = videoView.currentPosition
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    vm.currentVideo.value?.let { vm.openVideo(video = it) }
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