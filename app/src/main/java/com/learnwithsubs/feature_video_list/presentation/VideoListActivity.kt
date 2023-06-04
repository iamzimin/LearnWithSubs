package com.learnwithsubs.feature_video_list.presentation

import VideoListPicker
import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.learnwithsubs.R
import com.learnwithsubs.app.App
import com.learnwithsubs.databinding.VideoListBinding
import com.learnwithsubs.feature_video_list.presentation.adapter.VideoListAdapter
import com.learnwithsubs.feature_video_list.presentation.videos.VideoListViewModel
import com.learnwithsubs.feature_video_list.presentation.videos.VideoListViewModelFactory
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
        val menuButton = findViewById<ImageButton>(R.id.menu_button)

        val recyclerView = findViewById<RecyclerView>(R.id.video_list)
        (recyclerView?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false


        (applicationContext as App).videoListAppComponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)[VideoListViewModel::class.java]

        val PERMISSION_REQUEST_CODE = 123

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }


        uploadVideoButton.setOnClickListener {
            videoListPicker.pickVideo()
        }

        menuButton.setOnClickListener {
            openMenu()
        }


        vm.videoList.observe(this) { video ->
            adapter.updateData(ArrayList(video))
        }

        vm.videoToUpdate.observe(this) { video ->
            adapter.updateVideo(video)
        }

        vm.videoProgressLiveData.observe(this) { videoProgress ->
            if (videoProgress != null) {
                adapter.updateVideo(videoProgress)
            }
        }

    }


    private fun openMenu() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.video_list_menu)


//        val sort = dialog.findViewById<Button>(R.id.button1)
//        val select = dialog.findViewById<Button>(R.id.button2)
//        val delete = dialog.findViewById<Button>(R.id.button3)
//        sort.setOnClickListener {
//            Toast.makeText(applicationContext, "sort.text", Toast.LENGTH_SHORT).show()
//        }
//        select.setOnClickListener {
//            Toast.makeText(applicationContext, "select.text", Toast.LENGTH_SHORT).show()
//        }
//        delete.setOnClickListener {
//            Toast.makeText(applicationContext, "delete.text", Toast.LENGTH_SHORT).show()
//        }
        dialog.show()
        if (dialog.window != null) {
            dialog.show()
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setGravity(Gravity.BOTTOM)
        }
    }


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