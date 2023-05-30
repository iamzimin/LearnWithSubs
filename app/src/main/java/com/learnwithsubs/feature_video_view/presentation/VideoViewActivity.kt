package com.learnwithsubs.feature_video_view.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.View
import android.view.Window
import android.view.WindowManager
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
        configSystemUI()
        setContentView(R.layout.video_view)

//        val decorView = window.decorView
//        decorView.systemUiVisibility =
//            (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY // Set the content to appear under the system bars so that the
//                    // content doesn't resize when the system bars hide and show.
//                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Hide the nav bar and status bar
//                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    or View.SYSTEM_UI_FLAG_FULLSCREEN)


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

        val subtitleTextView = findViewById<TextView>(R.id.subtitle)

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
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= 33)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_VIDEO), STORAGE_PERMISSION_REQUEST_CODE)
            else
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_REQUEST_CODE)
        }
        else
            vm.currentVideo.value?.let { vm.openVideo(video = it) }


        // Live Data - Click Listener

        // URI listener
        vm.videoPath.observe(this) { path ->
            val videoPath = Uri.parse(path)
            videoView.setVideoURI(videoPath)
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
        val timeUpdate =
            Handler(Looper.getMainLooper()) // TODO может не правильно в mvvm (возможна онибка с некорректным отображением времени)
        timeUpdate.post(object : Runnable {
            override fun run() {
                vm.updateCurrentTime(videoView.currentPosition)
                timeUpdate.postDelayed(this, 1000)
            }
        })
        vm.videoTime.observe(this) { time ->
            videoTime.text = time
        }


        // Video subtitle update
        val subtitleUpdate =
            Handler(Looper.getMainLooper()) // TODO может не правильно в mvvm (возможна онибка с некорректным отображением времени)
        subtitleUpdate.post(object : Runnable {
            override fun run() {
                val sub = vm.getCurrentSubtitles(videoView.currentPosition.toLong())
                val spannableString = SpannableString(sub)
                val backgroundColor = BackgroundColorSpan(Color.BLACK)
                spannableString.setSpan(
                    backgroundColor,
                    0,
                    sub.length,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )

                subtitleTextView.text = spannableString
                subtitleUpdate.postDelayed(this, 300)
            }
        })


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
        videoView.setOnPreparedListener { vid ->
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
            videoControls.visibility = if (isButtonsShowed) View.VISIBLE else View.GONE
            if (isButtonsShowed) {
                Handler().postDelayed({
                    runOnUiThread {
                        videoControls.visibility = View.GONE
                        vm.isButtonsShowed.value = false
                    }
                }, 5000)
            }
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
                visibility = if (isPlaying) View.GONE else View.VISIBLE
            }
            pauseVideoButton.apply {
                visibility = if (!isPlaying) View.GONE else View.VISIBLE
            }
        }


        // Exit
        exitVideoView.setOnClickListener {
            finish()
        }
    }

    private fun configSystemUI() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.argb(128, 0, 0, 0)
    }

    override fun onStop() {
        super.onStop()
        vm.currentVideo.value?.let { vm.saveVideo(it) }
    }

    override fun onRestart() {
        super.onRestart()
        vm.currentVideo.value?.let { vm.openVideo(video = it) }
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