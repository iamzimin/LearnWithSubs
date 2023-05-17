package com.learnwithsubs.feature_video.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnwithsubs.R
import com.learnwithsubs.databinding.VideoListBinding
import com.learnwithsubs.feature_video.presentation.adapter.VideoAdapter
import com.learnwithsubs.feature_video.presentation.adapter.VideoAdapter.RecyclerViewItemDecoration
import com.learnwithsubs.feature_video.presentation.app.VideoApp
import com.learnwithsubs.feature_video.presentation.videos.VideoListViewModel
import com.learnwithsubs.feature_video.presentation.videos.VideoListViewModelFactory
import javax.inject.Inject
import kotlin.random.Random

//@AndroidEntryPoint
class VideoListActivity : AppCompatActivity() {
//    lateinit var binding: VideoListBinding
//    private val adapter = VideoAdapter()

    @Inject
    lateinit var vmFactory: VideoListViewModelFactory

    private lateinit var vm: VideoListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_list)
        supportActionBar?.hide()

        (applicationContext as VideoApp).videoAppComponent.inject(this)

        vm = ViewModelProvider(this, vmFactory)
            .get(VideoListViewModel::class.java)

        //binding = VideoListBinding.inflate(layoutInflater)
        //setContentView(vm.binding.root)
        //test()
    }

    /*
    private fun test() {
        binding.videoList.layoutManager = LinearLayoutManager(this@VideoListActivity)
        binding.videoList.adapter = adapter
        val itemDecoration = RecyclerViewItemDecoration(16)
        binding.videoList.addItemDecoration(itemDecoration)

        repeat(15) {
            adapter.addVideo(Random.nextInt(1, 4))
        }
    }
     */
}