package com.learnwithsubs.feature_video_list.presentation

import VideoPicker
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnwithsubs.R
import com.learnwithsubs.databinding.VideoListBinding
import com.learnwithsubs.feature_video_list.presentation.adapter.VideoAdapter
import com.learnwithsubs.feature_video_list.presentation.app.VideoApp
import com.learnwithsubs.feature_video_list.presentation.videos.VideoListViewModel
import com.learnwithsubs.feature_video_list.presentation.videos.VideoListViewModelFactory
import javax.inject.Inject


class VideoListActivity : AppCompatActivity() {
    private val PICK_VIDEO_REQUEST = 1
    private val videoPicker = VideoPicker(this, PICK_VIDEO_REQUEST)

    @Inject
    lateinit var vmFactory: VideoListViewModelFactory
    private lateinit var vm: VideoListViewModel

    private val adapter = VideoAdapter(videoListInit = ArrayList())
    private lateinit var binding: VideoListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_list)
        supportActionBar?.hide()
        setupRecyclerView()

        val uploadVideoButton = findViewById<CardView>(R.id.button_video_upload)
        uploadVideoButton.setOnClickListener {
            videoPicker.pickVideo()
        }

        (applicationContext as VideoApp).videoAppComponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)
            .get(VideoListViewModel::class.java)

        vm.videoList.observe(this, Observer { video ->
            adapter.updateData(ArrayList(video))
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        videoPicker.onActivityResult(requestCode, resultCode, data, vm, applicationContext)
    }

    private fun setupRecyclerView() {
        binding = VideoListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.videoList.layoutManager = LinearLayoutManager(this@VideoListActivity)
        binding.videoList.adapter = adapter
        val itemDecoration = VideoAdapter.RecyclerViewItemDecoration(16)
        binding.videoList.addItemDecoration(itemDecoration)
    }
}