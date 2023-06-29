package com.learnwithsubs.feature_video_list

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.learnwithsubs.R
import com.learnwithsubs.databinding.VideoListFragmentBinding
import com.learnwithsubs.feature_video_list.util.OrderType
import com.learnwithsubs.feature_video_list.util.VideoOrder
import com.learnwithsubs.feature_video_list.adapter.VideoListAdapter
import com.learnwithsubs.feature_video_list.models.VideoErrorType
import com.learnwithsubs.feature_video_list.videos.VideoListViewModel
import com.learnwithsubs.feature_video_list.videos.VideoListViewModelFactory
import com.learnwithsubs.feature_video_list.videos.VideosEvent


class VideoListFragment : Fragment() {
    private val PICK_VIDEO_REQUEST = 1
    private lateinit var videoListPicker: VideoListPicker

    private lateinit var videoListActivity: VideoListActivity
    private lateinit var vmFactory: VideoListViewModelFactory
    private lateinit var vm: VideoListViewModel
    private lateinit var binding: VideoListFragmentBinding

    val adapter = VideoListAdapter(videoListInit = ArrayList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        videoListActivity = requireActivity() as VideoListActivity
        binding = VideoListFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        setupRecyclerView()

        vmFactory = videoListActivity.vmFactory
        vm = ViewModelProvider(videoListActivity, vmFactory)[VideoListViewModel::class.java]

        videoListPicker = VideoListPicker(this, PICK_VIDEO_REQUEST)

        val searchEditText = view.findViewById<EditText>(R.id.search_edit_text)
        val uploadVideoButton = view.findViewById<CardView>(R.id.button_video_upload)
        val menuButton = view.findViewById<ImageButton>(R.id.menu_button)

        val recyclerView = view.findViewById<RecyclerView>(R.id.video_list)
        (recyclerView?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false


        uploadVideoButton.setOnClickListener {
            videoListPicker.pickVideo()
        }

        menuButton.setOnClickListener {
            openMenu()
        }
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //vm.filterVideo(filter = s.toString()) /!/
                vm.onEvent(event = VideosEvent.Filter(filter = s.toString()))
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        searchEditText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                textView.clearFocus()
                true
            } else false
        }

        vm.errorTypeLiveData.observe(videoListActivity) { video ->
            val errorType = video.errorType ?: return@observe
            when (errorType) {
                VideoErrorType.EXTRACTING_AUDIO ->      Toast.makeText(this@VideoListFragment.context, getString(R.string.audio_extraction_error), Toast.LENGTH_SHORT).show()
                VideoErrorType.DECODING_VIDEO ->        Toast.makeText(this@VideoListFragment.context, getString(R.string.video_decoding_error), Toast.LENGTH_SHORT).show()
                VideoErrorType.GENERATING_SUBTITLES ->  Toast.makeText(this@VideoListFragment.context, getString(R.string.subtitle_generation_error), Toast.LENGTH_SHORT).show()
                VideoErrorType.UPLOADING_AUDIO ->       Toast.makeText(this@VideoListFragment.context, getString(R.string.audio_upload_error), Toast.LENGTH_SHORT).show()
            }
            vm.onEvent(event = VideosEvent.DeleteVideo(video = video))
        }

        vm.videoList.observe(videoListActivity) { video ->
            if (video != null)
                adapter.updateData(ArrayList(video))
        }

        vm.videoToUpdate.observe(videoListActivity) { video ->
            adapter.updateVideo(video)
        }

        vm.videoProgressLiveData.observe(videoListActivity) { videoProgress ->
            if (videoProgress != null) {
                adapter.updateVideo(videoProgress)
            }
        }

        return binding.root
    }




    private fun openMenu() {
        val dialog = Dialog(videoListActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.video_list_menu_dialog)
        val isNeedSelect = adapter.videoSelected.size == adapter.videoList.size

        val sort = dialog.findViewById<CardView>(R.id.sort_by_card)
        val select = dialog.findViewById<CardView>(R.id.de_select_all_card)
        val rename = dialog.findViewById<CardView>(R.id.rename_card)
        val delete = dialog.findViewById<CardView>(R.id.delete_card)

        val selectText = dialog.findViewById<TextView>(R.id.de_select_all_text)
        selectText.text = if (isNeedSelect) videoListActivity.applicationContext.getString(R.string.deselect_all) else videoListActivity.applicationContext.getString(
            R.string.select_all
        )
        rename.visibility = if (adapter.videoSelected.size == 1) View.VISIBLE else View.GONE

        sort.setOnClickListener {
            openSortByMenu()
            dialog.dismiss()
        }

        select.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (isNeedSelect) {
                    adapter.clearSelect()
                    vm.onEvent(event = VideosEvent.DeSelect(isNeedSelect = false))
                }
                else {
                    adapter.isNormalMode = false
                    vm.onEvent(event = VideosEvent.DeSelect(isNeedSelect = true))
                    adapter.videoSelected = ArrayList(adapter.videoList)
                }
                dialog.dismiss()
            }
        })

        rename.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (adapter.videoSelected.size == 1)
                    vm.editableVideo = adapter.videoSelected[0]
                openRenameMenu()
                dialog.dismiss()
            }
        })

        delete.setOnClickListener {
            //vm.deleteSelectedVideo() /!/
            vm.onEvent(event = VideosEvent.DeleteSelectedVideos(videos = vm.videoList.value?.filter { it.isSelected }))
            adapter.clearSelect()
            dialog.dismiss()
        }

        dialog.show()
        if (dialog.window != null) {
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setGravity(Gravity.BOTTOM)
        }
    }

    private fun openRenameMenu() {
        val renameMenu = Dialog(videoListActivity)
        renameMenu.requestWindowFeature(Window.FEATURE_NO_TITLE)
        renameMenu.setContentView(R.layout.video_list_menu_rename_dialog)

        val editText = renameMenu.findViewById<EditText>(R.id.video_name_edittext)
        editText.setText(vm.editableVideo?.name)

        editText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                textView.clearFocus()
                val video = vm.videoList.value?.find { video -> video.id == vm.editableVideo?.id }
                if (video == null) {  //TODO add toast
                    adapter.clearSelect()
                    renameMenu.dismiss()
                    return@setOnEditorActionListener true
                }
                video.name = textView.text.toString()
                video.isSelected = false
                vm.onEvent(event = VideosEvent.RenameVideo(video = video))
                adapter.clearSelect()
                renameMenu.dismiss()
                true
            } else false
        }

        renameMenu.show()
        if (renameMenu.window != null) {
            renameMenu.window?.attributes?.windowAnimations = 0
            renameMenu.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            renameMenu.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun openSortByMenu() {
        val sortByDialog = Dialog(videoListActivity)
        sortByDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        sortByDialog.setContentView(R.layout.video_list_menu_sort_by_dialog)

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

        fun setButtonColors(ascending: Boolean) {
            ascendingButton.setBackgroundColor(if (ascending) videoListActivity.applicationContext.getColor(
                R.color.button_pressed
            ) else videoListActivity.applicationContext.getColor(R.color.button_normal))
            descendingButton.setBackgroundColor(if (ascending) videoListActivity.applicationContext.getColor(
                R.color.button_normal
            ) else videoListActivity.applicationContext.getColor(R.color.button_pressed))
        }

        nameCheckBox.isChecked = vm.sortMode.value is VideoOrder.Name
        dateCheckBox.isChecked = vm.sortMode.value is VideoOrder.Date
        durationCheckBox.isChecked = vm.sortMode.value is VideoOrder.Duration

        val sortType = vm.sortMode.value ?: VideoListViewModel.DEFAULT_SORT_MODE
        setButtonColors(ascending = sortType.orderType is OrderType.Ascending)

        ascendingButton.setOnClickListener {
            setButtonColors(ascending = true)
            val currentOrderType = vm.sortMode.value?.apply { orderType = OrderType.Ascending } ?: VideoListViewModel.DEFAULT_SORT_MODE
            //vm.setSortMode(currentOrderType) /!/
            vm.onEvent(event = VideosEvent.SetOrderMode(orderMode = currentOrderType))
        }

        descendingButton.setOnClickListener {
            setButtonColors(ascending = false)
            val currentOrderType = vm.sortMode.value?.apply { orderType = OrderType.Descending } ?: VideoListViewModel.DEFAULT_SORT_MODE
            //vm.setSortMode(currentOrderType) /!/
            vm.onEvent(event = VideosEvent.SetOrderMode(orderMode = currentOrderType))
        }

        nameCardView.setOnClickListener {
            val currentOrderType = vm.sortMode.value?.orderType ?: VideoListViewModel.DEFAULT_SORT_MODE.orderType
            //vm.setSortMode(VideoOrder.Name(currentOrderType)) /!/
            vm.onEvent(event = VideosEvent.SetOrderMode(orderMode = VideoOrder.Name(orderType = currentOrderType)))
        }
        dateCardView.setOnClickListener {
            val currentOrderType = vm.sortMode.value?.orderType ?: VideoListViewModel.DEFAULT_SORT_MODE.orderType
            //vm.setSortMode(VideoOrder.Date(currentOrderType)) /!/
            vm.onEvent(event = VideosEvent.SetOrderMode(orderMode = VideoOrder.Date(orderType = currentOrderType)))
        }
        durationCardView.setOnClickListener {
            val currentOrderType = vm.sortMode.value?.orderType ?: VideoListViewModel.DEFAULT_SORT_MODE.orderType
            //vm.setSortMode(VideoOrder.Duration(currentOrderType)) /!/
            vm.onEvent(event = VideosEvent.SetOrderMode(orderMode = VideoOrder.Duration(orderType = currentOrderType)))
        }

        clearButton.setOnClickListener {
            //vm.setSortMode(VideoListViewModel.DEFAULT_SORT_MODE) /!/
            vm.onEvent(event = VideosEvent.SetOrderMode(orderMode = VideoListViewModel.DEFAULT_SORT_MODE))
        }
        applyButton.setOnClickListener {
            //vm.updateVideoList(videoOrder = vm.sortMode.value, filter = vm.filter) /!/
            vm.onEvent(event = VideosEvent.UpdateVideoList(videoOrder = vm.sortMode.value, filter = vm.filter))
            sortByDialog.dismiss()
        }

        vm.sortMode.observe(videoListActivity) { sortMode ->
            nameCheckBox.isChecked = sortMode is VideoOrder.Name
            dateCheckBox.isChecked = sortMode is VideoOrder.Date
            durationCheckBox.isChecked = sortMode is VideoOrder.Duration
        }

        sortByDialog.show()
        if (sortByDialog.window != null) {
            sortByDialog.window?.attributes?.windowAnimations = 0
            sortByDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            sortByDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            sortByDialog.window?.setGravity(Gravity.BOTTOM)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        videoListPicker.loadVideoOnResult(requestCode, resultCode, data, vm, videoListActivity)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(videoListActivity)
        binding.videoList.layoutManager = layoutManager
        binding.videoList.adapter = adapter
        val itemDecoration = VideoListAdapter.RecyclerViewItemDecoration(16)
        binding.videoList.addItemDecoration(itemDecoration)
    }
}