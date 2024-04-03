package com.learnwithsubs.video_view.presentation

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.learnwithsubs.video_view.R
import com.learnwithsubs.video_view.databinding.ActivityVideoViewBinding
import com.learnwithsubs.video_view.databinding.VideoViewInterfaceBinding
import com.learnwithsubs.video_view.di.DaggerVideoViewAppComponent
import com.learnwithsubs.video_view.di.VideoViewAppModule
import com.learnwithsubs.video_view.presentation.videos.VideoViewViewModel
import com.learnwithsubs.video_view.presentation.videos.VideoViewViewModelFactory
import javax.inject.Inject


class VideoViewActivity : AppCompatActivity() {
    companion object {
        private const val STORAGE_PERMISSION_REQUEST_CODE = 1
    }

    @Inject
    lateinit var vmFactory: VideoViewViewModelFactory
    private lateinit var vm: VideoViewViewModel

    private lateinit var videoViewBind: ActivityVideoViewBinding
    private lateinit var videoViewIBind: VideoViewInterfaceBinding
    private lateinit var videoView: VideoView
    private var currentPosition = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerVideoViewAppComponent.builder().videoViewAppModule(VideoViewAppModule(context = this))
            .build().inject(this)
        vm = ViewModelProvider(this, vmFactory)[VideoViewViewModel::class.java]

        configSystemUI()
        setContentView(R.layout.activity_video_view)
        val videoViewLayout = findViewById<ConstraintLayout>(R.id.video_view_constraint_layout)

        val translateDialog = TranslateDialog(activity = this@VideoViewActivity, vm = vm)
        videoViewBind = ActivityVideoViewBinding.inflate(layoutInflater, videoViewLayout, true)
        videoViewIBind = videoViewBind.videoViewInterface
        videoView = videoViewBind.videoView


        vm.initCurrentVideo(videoId = intent.getIntExtra("videoID", -1))

        videoViewBind.subtitle.setBackgroundResource(android.R.color.black)
        val subtitleLP = videoViewBind.subtitle.layoutParams as ConstraintLayout.LayoutParams

        // Set VideoView
        val mediaController = CustomMediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        setVideoViewConfiguration(config = resources.configuration)


        //Video Play
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_VIDEO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= 33)
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_MEDIA_VIDEO),
                    STORAGE_PERMISSION_REQUEST_CODE
                )
            else
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_REQUEST_CODE
                )
        } else
            vm.currentVideo?.let { vm.openVideo(video = it, isPlaying = true) }

        videoViewBind.subtitle.setCustomSelectionActionModeCallback(object : ActionMode.Callback {
            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                menu.clear()
                menu.add(R.string.translate)

                val subMenu = menu.addSubMenu(R.string.submenu)
                subMenu.add(Menu.NONE, android.R.id.selectAll, 1, R.string.select_all)
                subMenu.add(Menu.NONE, android.R.id.copy, 2, R.string.copy)
                subMenu.add(Menu.NONE, android.R.id.shareText, 3, R.string.share)
                return true
            }

            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                return true
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                when (item.title.toString()) {
                    getString(R.string.translate) -> {
                        val sub = videoViewBind.subtitle
                        val selectedText = sub.text.substring(sub.selectionStart, sub.selectionEnd)
                        vm.textToTranslate = selectedText
                        translateDialog.openTranslateDialog()
                        return true
                    }
                }
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode) {}
        })


        // Live Data - Click Listener

        // URI listener
        vm.videoPath.observe(this) { path ->
            val videoPath = Uri.parse(path)
            videoView.setVideoURI(videoPath)
            videoView.requestFocus()
            videoView.start()
            val watchProgress = vm.currentVideo?.watchProgress ?: 0
            videoView.seekTo(watchProgress)
        }


        // Video name
        vm.videoName.observe(this) { name ->
            videoViewIBind.videoName.text = name
        }

        // Subtitle error
        vm.subtitleError.observe(this@VideoViewActivity) { error ->
            Toast.makeText(
                this.applicationContext,
                R.string.wrong_subtitle_format,
                Toast.LENGTH_SHORT
            ).show()
        }


        // Video time update
        val timeUpdate = Handler(Looper.getMainLooper())
        timeUpdate.post(object : Runnable {
            override fun run() {
                vm.updateCurrentTime(videoView.currentPosition)
                timeUpdate.postDelayed(this, 1000)
            }
        })
        vm.videoTime.observe(this) { time ->
            videoViewIBind.videoTime.text = time
        }


        // Video subtitle update
        val subtitleUpdate = Handler(Looper.getMainLooper())
        subtitleUpdate.post(object : Runnable {
            override fun run() {
                if (videoView.isPlaying) {
                    val sub = vm.getCurrentSubtitles(videoView.currentPosition.toLong())
                    /*
                    val spannableString = SpannableString(sub)
                    val backgroundColor = ForegroundColorSpan(Color.BLACK)
                    spannableString.setSpan(
                        backgroundColor,
                        0,
                        sub.length,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE
                    )*/
                    videoViewBind.subtitle.text = sub
                }
                subtitleUpdate.postDelayed(this, 300)
            }
        })


        // Seek bar
        vm.videoSeekBarProgress.observe(this) { progress ->
            videoViewIBind.videoPlaySeekBar.progress = progress
        }
        videoViewIBind.videoPlaySeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val newPosition = (videoView.duration * progress) / 100
                    videoView.seekTo(newPosition)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


        // Button show - Subtitles position
        var timer: CountDownTimer? = null
        videoViewBind.videoViewConstraintLayout.setOnClickListener {
            vm.isButtonsShowedLiveData.value = vm.isButtonsShowedLiveData.value != true
        }
        vm.isButtonsShowedLiveData.observe(this) { isButtonsShowed ->
            videoViewIBind.videoControls.visibility =
                if (isButtonsShowed) View.VISIBLE else View.GONE
            if (isButtonsShowed) {
                timer = object : CountDownTimer(5000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        if (!videoView.isPlaying) return
                        videoViewIBind.videoControls.visibility = View.GONE
                        vm.isButtonsShowedLiveData.value = false
                    }
                }.start()
                subtitleLP.setMargins(
                    subtitleLP.leftMargin,
                    subtitleLP.topMargin,
                    subtitleLP.rightMargin,
                    resources.getDimensionPixelSize(R.dimen.subtitle_indentation_with_interface)
                )
                videoViewBind.subtitle.layoutParams = subtitleLP
            } else {
                timer?.cancel()
                subtitleLP.setMargins(
                    subtitleLP.leftMargin,
                    subtitleLP.topMargin,
                    subtitleLP.rightMargin,
                    resources.getDimensionPixelSize(R.dimen.subtitle_indentation_without_interface)
                )
                videoViewBind.subtitle.layoutParams = subtitleLP
            }
        }


        // Button forward
        videoView.setOnPreparedListener { vid ->
            videoViewIBind.forward5VideoButton.setOnClickListener {
                timer?.start()
                val new = vid.currentPosition + 5000
                vid.seekTo(new)
            }
            videoViewIBind.rewind5VideoButton.setOnClickListener {
                timer?.start()
                val new = vid.currentPosition - 5000
                vid.seekTo(new)
            }
        }

        videoViewIBind.videoMenuButton.setOnClickListener {
            timer?.cancel()
            vm.videoPlaying.value = false
        }

        // Video play/pause
        videoViewIBind.pauseVideoButton.setOnClickListener {
            vm.videoPlaying.value = vm.videoPlaying.value != true
            timer?.start()
        }
        videoViewIBind.playVideoButton.setOnClickListener {
            vm.videoPlaying.value = vm.videoPlaying.value != true
            timer?.start()
        }
        vm.videoPlaying.observe(this) { isPlaying ->
            videoView.apply {
                if (isPlaying) start() else pause()
            }
            videoViewIBind.playVideoButton.apply {
                visibility = if (isPlaying) View.GONE else View.VISIBLE
            }
            videoViewIBind.pauseVideoButton.apply {
                visibility = if (!isPlaying) View.GONE else View.VISIBLE
            }
        }

        // Exit
        videoViewIBind.exitVideoView.setOnClickListener {
            finish()
        }
    }


    private fun setVideoViewConfiguration(config: Configuration) {
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val layoutParams = videoView.layoutParams
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            videoView.layoutParams = layoutParams
        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val layoutParams = videoView.layoutParams
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            videoView.layoutParams = layoutParams
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
        vm.currentVideo?.let { vm.saveVideo(it) }
    }

    override fun onRestart() {
        super.onRestart()
        vm.currentVideo?.let { vm.openVideo(video = it, isPlaying = false) }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        currentPosition = videoView.currentPosition
        setVideoViewConfiguration(config = newConfig)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    vm.currentVideo?.let { vm.openVideo(video = it, isPlaying = true) }
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