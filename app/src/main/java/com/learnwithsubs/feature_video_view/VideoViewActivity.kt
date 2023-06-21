package com.learnwithsubs.feature_video_view

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnwithsubs.R
import com.learnwithsubs.app.App
import com.learnwithsubs.databinding.TranslateDialogBinding
import com.learnwithsubs.feature_video_view.adapter.DictionaryAdapter
import com.learnwithsubs.feature_video_view.adapter.OnDictionaryClick
import com.learnwithsubs.feature_video_view.videos.VideoViewViewModel
import com.learnwithsubs.feature_video_view.videos.VideoViewViewModelFactory
import java.util.Locale
import javax.inject.Inject


class VideoViewActivity : AppCompatActivity(), OnDictionaryClick {
    companion object {
        private const val STORAGE_PERMISSION_REQUEST_CODE = 1
    }

    @Inject
    lateinit var vmFactory: VideoViewViewModelFactory
    private lateinit var vm: VideoViewViewModel

    private lateinit var translateDialogBinding: TranslateDialogBinding
    private lateinit var videoView: VideoView
    private var currentPosition = 0

    private lateinit var renameMenu: Dialog
    private val dictionaryAdapter = DictionaryAdapter(wordsInit = ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configSystemUI()
        setContentView(R.layout.video_view)

        translateDialogBinding = TranslateDialogBinding.inflate(layoutInflater)
        renameMenu = Dialog(this@VideoViewActivity)
        setupTranslateDialog()

        // Set VM
        (applicationContext as App).videoViewAppComponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)[VideoViewViewModel::class.java]
        vm.currentVideo.value = intent.getParcelableExtra("videoData")
        getLanguageFromSettings()


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
        subtitleTextView.setBackgroundResource(android.R.color.black)
        val layoutParams = subtitleTextView.layoutParams as ConstraintLayout.LayoutParams

        val videoTime = findViewById<TextView>(R.id.video_time)
        val videoPlaySeekBar = findViewById<SeekBar>(R.id.video_play_status)

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

        subtitleTextView.setCustomSelectionActionModeCallback(object : ActionMode.Callback {
            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                menu.clear()
                menu.add(R.string.translate)

                val subMenu = menu.addSubMenu("") // TODO
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
                        val selectedText = subtitleTextView.text.substring(subtitleTextView.selectionStart, subtitleTextView.selectionEnd)
                        vm.textToTranslate = selectedText
                        Toast.makeText(this@VideoViewActivity, selectedText, Toast.LENGTH_SHORT).show()
                        openTranslateDialog()
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
            val watchProgress = vm.currentVideo.value?.watchProgress ?: 0
            videoView.seekTo(watchProgress)
        }


        // Video name
        vm.videoName.observe(this) { name ->
            videoName.text = name
        }


        // Video time update
        val timeUpdate = Handler(Looper.getMainLooper()) // TODO может не правильно в mvvm (возможна онибка с некорректным отображением времени)
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
        val subtitleUpdate = Handler(Looper.getMainLooper()) // TODO может не правильно в mvvm (возможна онибка с некорректным отображением времени)
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
                    subtitleTextView.text = sub
                }
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


        // Button forward
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


        // Button show - Subtitles position
        var timer: CountDownTimer? = null
        videoView.setOnClickListener {
            vm.isButtonsShowedLiveData.value = vm.isButtonsShowedLiveData.value != true
        }
        vm.isButtonsShowedLiveData.observe(this) { isButtonsShowed ->
            videoControls.visibility = if (isButtonsShowed) View.VISIBLE else View.GONE
            if (isButtonsShowed) {
                timer = object : CountDownTimer(5000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        if (!videoView.isPlaying) return
                        videoControls.visibility = View.GONE
                        vm.isButtonsShowedLiveData.value = false
                    }
                }.start()
                layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin, 180)
                subtitleTextView.layoutParams = layoutParams
            }
            else {
                timer?.cancel()
                layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin, 40)
                subtitleTextView.layoutParams = layoutParams
            }
        }

        // Video play/pause
        pauseVideoButton.setOnClickListener {
            vm.videoPlaying.value = vm.videoPlaying.value != true
            timer?.start()
        }
        playVideoButton.setOnClickListener {
            vm.videoPlaying.value = vm.videoPlaying.value != true
            timer?.start()
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


        // Translate
        vm.dictionaryTranslationLiveData.observe(this@VideoViewActivity) { transl ->
            if (transl != null) {
                translateDialogBinding.outputWord.setText(transl)
                translateDialogBinding.outputWord.clearFocus()
            } else {
                vm.getWordsFromTranslator(
                    word = vm.textToTranslate,
                    learnLanguage = vm.learnLanguage
                )
            }
        }
        vm.dictionarySynonymsLiveData.observe(this@VideoViewActivity) { dictList ->
            dictionaryAdapter.updateData(wordsList = dictList)
        }
        vm.translatorTranslationLiveData.observe(this@VideoViewActivity) { transl ->
            translateDialogBinding.outputWord.setText(transl)
            translateDialogBinding.outputWord.clearFocus()
        }
    }

    private fun openTranslateDialog() {
        vm.getWordsFromDictionary(
            key = TranslationKeyAPI.YANDEX_DICTIONARY_KEY,
            inputLang = vm.nativeLanguage,
            outputLang = vm.learnLanguage,
            word = vm.textToTranslate
        )

        translateDialogBinding.inputWord.setText(vm.textToTranslate)
        translateDialogBinding.inputWord.clearFocus()

        renameMenu.setOnDismissListener {
            translateDialogBinding.inputWord.setText("")
            translateDialogBinding.outputWord.setText("")
            dictionaryAdapter.updateData(wordsList = ArrayList())
        }
        renameMenu.show()
    }

    private fun getLanguageFromSettings() {
        val nativeLanguage = R.string.english  // TODO взять язык из настроек
        val learnLanguage = R.string.russian // TODO взять язык из настроек

        translateDialogBinding.inputLanguage.text = getString(nativeLanguage)
        translateDialogBinding.outputLanguage.text = getString(learnLanguage)

        val config = Configuration(resources.configuration)
        config.setLocale(Locale("en"))
        val englishResources = createConfigurationContext(config).resources

        vm.nativeLanguage = englishResources.getString(nativeLanguage)
        vm.learnLanguage = englishResources.getString(learnLanguage)
    }

    private fun setupTranslateDialog() {
        renameMenu.requestWindowFeature(Window.FEATURE_NO_TITLE)
        renameMenu.setContentView(R.layout.translate_dialog)

        dictionaryAdapter.setOnItemClickListener(this@VideoViewActivity)

        renameMenu.setContentView(translateDialogBinding.root)
        translateDialogBinding.dictionaryRecycler.layoutManager = LinearLayoutManager(this)
        translateDialogBinding.dictionaryRecycler.adapter = dictionaryAdapter
        val itemDecoration = DictionaryAdapter.RecyclerViewItemDecoration(16)
        translateDialogBinding.dictionaryRecycler.addItemDecoration(itemDecoration)

        if (renameMenu.window != null) {
            renameMenu.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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

    override fun onItemClick(similarWord: String, similarWordTranslate: String) {
        translateDialogBinding.inputWord.setText(similarWord)
        translateDialogBinding.outputWord.setText(similarWordTranslate)
    }

}