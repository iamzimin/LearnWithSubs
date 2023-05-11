package com.learnwithsubs.feature_video.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnwithsubs.R
import com.learnwithsubs.databinding.VideoListBinding
import com.learnwithsubs.feature_video.presentation.adapter.VideoAdapter
import com.learnwithsubs.feature_video.presentation.adapter.VideoAdapter.RecyclerViewItemDecoration
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    lateinit var binding: VideoListBinding
    private val adapter = VideoAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_list)
        supportActionBar?.hide()
        binding = VideoListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        test()
    }

    private fun test() {
        binding.videoList.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.videoList.adapter = adapter
        val itemDecoration = RecyclerViewItemDecoration(16)
        binding.videoList.addItemDecoration(itemDecoration)

        repeat(15) {
            adapter.addVideo(Random.nextInt(1, 4))
        }
    }
}