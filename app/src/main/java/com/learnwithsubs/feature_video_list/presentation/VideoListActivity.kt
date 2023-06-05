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
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.learnwithsubs.R
import com.learnwithsubs.app.App
import com.learnwithsubs.databinding.VideoListBinding
import com.learnwithsubs.feature_video_list.domain.util.OrderType
import com.learnwithsubs.feature_video_list.domain.util.VideoOrder
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

    val adapter = VideoListAdapter(videoListInit = ArrayList())
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
            if (video != null)
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
        val isNeedSelect = adapter.videoSelected.size == adapter.videoList.size

        val sort = dialog.findViewById<CardView>(R.id.sort_by_card)
        val select = dialog.findViewById<CardView>(R.id.de_select_all_card)
        val delete = dialog.findViewById<CardView>(R.id.delete_card)

        val selectText = dialog.findViewById<TextView>(R.id.de_select_all_text)
        selectText.text = if (isNeedSelect) applicationContext.getString(R.string.deselect_all) else applicationContext.getString(R.string.select_all)



        sort.setOnClickListener {
            openSortByMenu()
            dialog.dismiss()
        }

        select.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (isNeedSelect) {
                    adapter.isNormalMode = true
                    adapter.videoSelected.clear()
                    vm.deSelectVideo(isNeedSelect = false)
                }
                else {
                    adapter.isNormalMode = false
                    vm.deSelectVideo(isNeedSelect = true)
                    adapter.videoSelected = ArrayList(adapter.videoList)
                }
                dialog.dismiss()
            }

        })

        delete.setOnClickListener {
            vm.deleteSelectedVideo()
            dialog.dismiss()
        }

        dialog.show()
        if (dialog.window != null) {
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setGravity(Gravity.BOTTOM)
        }
    }

    private fun openSortByMenu() {
        val sortByDialog = Dialog(this)
        sortByDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        sortByDialog.setContentView(R.layout.video_list_menu_sort_by)

        val ascendingButton = sortByDialog.findViewById<Button>(R.id.ascending)
        val descendingButton = sortByDialog.findViewById<Button>(R.id.descending)

        val clearButton = sortByDialog.findViewById<Button>(R.id.clear_button)
        val applyButton = sortByDialog.findViewById<Button>(R.id.apply_button)

        val nameCardView = sortByDialog.findViewById<CardView>(R.id.cardView_name)
        val dateCardView = sortByDialog.findViewById<CardView>(R.id.cardView_date)
        val durationCardView = sortByDialog.findViewById<CardView>(R.id.cardView_duration)

        val nameCheckBox = sortByDialog.findViewById<CheckBox>(R.id.name_check_box)
        val dateCheckBox = sortByDialog.findViewById<CheckBox>(R.id.date_check_box)
        val durationCheckBox = sortByDialog.findViewById<CheckBox>(R.id.duration_check_box)

        if (vm.sortMode.orderType == OrderType.Ascending)
            ascendingButton.setBackgroundColor(applicationContext.getColor(R.color.button_pressed))
        else if (vm.sortMode.orderType == OrderType.Descending)
            descendingButton.setBackgroundColor(applicationContext.getColor(R.color.button_pressed))

        when (vm.sortMode) {
            is VideoOrder.Name -> {
                nameCheckBox.isChecked = true
                dateCheckBox.isChecked = false
                durationCheckBox.isChecked = false
            }
            is VideoOrder.Date -> {
                nameCheckBox.isChecked = false
                dateCheckBox.isChecked = true
                durationCheckBox.isChecked = false
            }
            is VideoOrder.Duration -> {
                nameCheckBox.isChecked = false
                dateCheckBox.isChecked = false
                durationCheckBox.isChecked = true
            }
            else -> {}
        }


        ascendingButton.setOnClickListener {
            ascendingButton.setBackgroundColor(applicationContext.getColor(R.color.button_pressed))
            descendingButton.setBackgroundColor(applicationContext.getColor(R.color.button_normal))
            vm.sortMode.orderType = OrderType.Ascending
        }

        descendingButton.setOnClickListener {
            ascendingButton.setBackgroundColor(applicationContext.getColor(R.color.button_normal))
            descendingButton.setBackgroundColor(applicationContext.getColor(R.color.button_pressed))
            vm.sortMode.orderType = OrderType.Descending
        }

        clearButton.setOnClickListener {
            vm.sortMode = VideoOrder.Date(OrderType.Descending)
            ascendingButton.setBackgroundColor(applicationContext.getColor(R.color.button_normal))
            descendingButton.setBackgroundColor(applicationContext.getColor(R.color.button_pressed))
            nameCheckBox.isChecked = false
            dateCheckBox.isChecked = true
            durationCheckBox.isChecked = false
            sortByDialog.dismiss()
        }

        applyButton.setOnClickListener{
            vm.updateVideoList()
            sortByDialog.dismiss()
        }

        nameCardView.setOnClickListener {
            nameCheckBox.isChecked = true
            dateCheckBox.isChecked = false
            durationCheckBox.isChecked = false
            vm.sortMode = VideoOrder.Name(vm.sortMode.orderType)
        }

        dateCardView.setOnClickListener {
            nameCheckBox.isChecked = false
            dateCheckBox.isChecked = true
            durationCheckBox.isChecked = false
            vm.sortMode = VideoOrder.Date(vm.sortMode.orderType)
        }

        durationCardView.setOnClickListener {
            nameCheckBox.isChecked = false
            dateCheckBox.isChecked = false
            durationCheckBox.isChecked = true
            vm.sortMode = VideoOrder.Duration(vm.sortMode.orderType)
        }


        sortByDialog.show()
        if (sortByDialog.window != null) {
            sortByDialog.window?.attributes?.windowAnimations = 0
            sortByDialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            sortByDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            sortByDialog.window!!.setGravity(Gravity.BOTTOM)
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