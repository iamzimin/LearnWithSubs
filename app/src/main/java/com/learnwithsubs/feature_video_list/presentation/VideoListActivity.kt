package com.learnwithsubs.feature_video_list.presentation

import VideoListPicker
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.learnwithsubs.R
import com.learnwithsubs.databinding.VideoListBinding
import com.learnwithsubs.feature_video_list.presentation.adapter.VideoListAdapter
import com.learnwithsubs.app.App
import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.presentation.videos.VideoListViewModel
import com.learnwithsubs.feature_video_list.presentation.videos.VideoListViewModelFactory
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class VideoListActivity : AppCompatActivity() {
    private val PICK_VIDEO_REQUEST = 1
    private val videoListPicker = VideoListPicker(this, PICK_VIDEO_REQUEST)

    @Inject
    lateinit var vmFactory: VideoListViewModelFactory
    private lateinit var vm: VideoListViewModel

    private val adapter = VideoListAdapter(videoListInit = ArrayList())
    private lateinit var binding: VideoListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_list)
        supportActionBar?.hide()
        setupRecyclerView()

        val uploadVideoButton = findViewById<CardView>(R.id.button_video_upload)
        val recyclerView = findViewById<RecyclerView>(R.id.video_list)
        (recyclerView?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false


        uploadVideoButton.setOnClickListener {
            videoListPicker.pickVideo()
        }

        (applicationContext as App).videoListAppComponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)[VideoListViewModel::class.java]

        val PERMISSION_REQUEST_CODE = 123

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }

        vm.videoList.observe(this) { video ->
            adapter.updateData(ArrayList(video))
        }

        vm.getVideoFrameNumberLiveData().observe(this) { videoFrameNumber ->
            adapter.updateVideo(videoFrameNumber)
        }

    }

//    override fun onResume() {
//        super.onResume()
//        vm.updateList()
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        videoListPicker.onActivityResult(requestCode, resultCode, data, vm, applicationContext)
    }

    private fun setupRecyclerView() {
        binding = VideoListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.videoList.layoutManager = LinearLayoutManager(this@VideoListActivity)
        binding.videoList.adapter = adapter
        val itemDecoration = VideoListAdapter.RecyclerViewItemDecoration(16)
        binding.videoList.addItemDecoration(itemDecoration)
    }
}