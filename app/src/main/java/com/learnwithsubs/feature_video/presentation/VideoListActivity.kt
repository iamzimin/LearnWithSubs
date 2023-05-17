package com.learnwithsubs.feature_video.presentation

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnwithsubs.R
import com.learnwithsubs.databinding.VideoListBinding
import com.learnwithsubs.feature_video.domain.models.Video
import com.learnwithsubs.feature_video.presentation.adapter.VideoAdapter
import com.learnwithsubs.feature_video.presentation.app.VideoApp
import com.learnwithsubs.feature_video.presentation.videos.VideoListViewModel
import com.learnwithsubs.feature_video.presentation.videos.VideoListViewModelFactory
import javax.inject.Inject

class VideoListActivity : AppCompatActivity() {

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
            vm.test()
            //adapter.addVideo()
        }

        (applicationContext as VideoApp).videoAppComponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)
            .get(VideoListViewModel::class.java)

        vm.videoList.observe(this, Observer { video ->
            adapter.updateData(ArrayList(video))
        })



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