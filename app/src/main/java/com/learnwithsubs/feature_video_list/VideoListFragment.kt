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
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.learnwithsubs.R
import com.learnwithsubs.databinding.VideoListFragmentBinding
import com.learnwithsubs.feature_video_list.adapter.OnSelectChange
import com.learnwithsubs.feature_video_list.util.OrderType
import com.learnwithsubs.feature_video_list.util.VideoOrder
import com.learnwithsubs.feature_video_list.adapter.VideoListAdapter
import com.learnwithsubs.feature_video_list.models.VideoErrorType
import com.learnwithsubs.feature_video_list.models.VideoLoadingType
import com.learnwithsubs.feature_video_list.videos.VideoListViewModel
import com.learnwithsubs.feature_video_list.videos.VideoListViewModelFactory


class VideoListFragment : Fragment(), OnSelectChange {
    private val PICK_VIDEO_REQUEST = 1
    private lateinit var videoListPicker: VideoListPicker

    private lateinit var videoListActivity: VideoListActivity
    private lateinit var vmFactory: VideoListViewModelFactory
    private lateinit var vm: VideoListViewModel
    private lateinit var binding: VideoListFragmentBinding

    private val adapter = VideoListAdapter()
    private lateinit var searchEditText: EditText
    private lateinit var searchLayout: LinearLayout
    private lateinit var uploadVideoCard: CardView
    private lateinit var sortByButton: ImageButton
    private lateinit var closeSelectionButton: CardView

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var editListLayout: LinearLayout

    private lateinit var deSelectAllMenu: CardView
    private lateinit var deSelectAllMenuText: TextView
    private lateinit var renameMenu: CardView
    private lateinit var deleteMenu: CardView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        videoListActivity = requireActivity() as VideoListActivity
        binding = VideoListFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        setupRecyclerView()

        vmFactory = videoListActivity.vmFactory
        vm = ViewModelProvider(videoListActivity, vmFactory)[VideoListViewModel::class.java]

        videoListPicker = VideoListPicker(this, PICK_VIDEO_REQUEST)

        bottomNav = videoListActivity.findViewById<BottomNavigationView>(R.id.fragment_navigation)
        editListLayout = videoListActivity.findViewById<LinearLayout>(R.id.edit_list_layout)
        searchEditText = view.findViewById<EditText>(R.id.search_edit_text)
        searchLayout = view.findViewById<LinearLayout>(R.id.search_bar)

        uploadVideoCard = view.findViewById<CardView>(R.id.load_video_card)
        sortByButton = view.findViewById<ImageButton>(R.id.sort_by)
        closeSelectionButton = view.findViewById<CardView>(R.id.close_selection_mode)

        val recyclerView = view.findViewById<RecyclerView>(R.id.video_list)
        (recyclerView?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        deSelectAllMenu = videoListActivity.findViewById<CardView>(R.id.de_select_all_menu)
        deSelectAllMenuText = videoListActivity.findViewById<TextView>(R.id.de_select_all_menu_text)
        renameMenu = videoListActivity.findViewById<CardView>(R.id.rename_menu)
        deleteMenu = videoListActivity.findViewById<CardView>(R.id.delete_menu)


        uploadVideoCard.setOnClickListener {
            videoListPicker.pickVideo()
        }

        sortByButton.setOnClickListener {
            openSortByMenu()
        }
        closeSelectionButton.setOnClickListener{
            adapter.clearSelection()
        }
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vm.setFilterMode(filter = s.toString())
                vm.updateVideoList()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        searchEditText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                textView.clearFocus()
                true
            } else false
        }

        deSelectAllMenu.setOnClickListener {
            val isSelectAll = adapter.getSelectedVideoSize() == adapter.getVideoListSize()
            if (isSelectAll)
                adapter.deselectAll()
            else
                adapter.selectAll()
            onSelectAll(isSelectAll = !isSelectAll)
            onSingleSelected(isSingleSelected = false)
        }
        renameMenu.setOnClickListener {
            vm.editableVideo = adapter.getEditableVideo()
            openRenameMenu()
        }
        deleteMenu.setOnClickListener {
            vm.deleteSelectedVideo(selectedVideos = adapter.getSelectedVideo())
            adapter.changeMode(isNormalMode = true)
        }

        vm.errorTypeLiveData.value = null
        vm.errorTypeLiveData.observe(videoListActivity) { video ->
            if (video == null) return@observe
            val errorType = video.errorType ?: return@observe
            when (errorType) {
                VideoErrorType.PROCESSING_VIDEO ->      Toast.makeText(this@VideoListFragment.context, getString(R.string.video_processing_error), Toast.LENGTH_SHORT).show()
                VideoErrorType.EXTRACTING_AUDIO ->      Toast.makeText(this@VideoListFragment.context, getString(R.string.audio_extraction_error), Toast.LENGTH_SHORT).show()
                VideoErrorType.DECODING_VIDEO ->        Toast.makeText(this@VideoListFragment.context, getString(R.string.video_decoding_error), Toast.LENGTH_SHORT).show()
                VideoErrorType.GENERATING_SUBTITLES ->  Toast.makeText(this@VideoListFragment.context, getString(R.string.subtitle_generation_error), Toast.LENGTH_SHORT).show()
                VideoErrorType.UPLOADING_AUDIO ->       Toast.makeText(this@VideoListFragment.context, getString(R.string.audio_upload_error), Toast.LENGTH_SHORT).show()
            }
            vm.deleteVideo(video = video)
        }

        vm.videoList.observe(videoListActivity) { videoList ->
            videoList ?: return@observe
            val sorted = vm.getSortedVideoList(videoList = videoList)
            adapter.updateData(ArrayList(sorted.toList()))
        }

        vm.videoProgressLiveData.observe(videoListActivity) { videoProgress ->
            if (videoProgress != null)
                adapter.updateVideo(videoProgress.copy())
        }

        return binding.root
    }



/*
    private fun openMenu() {
        val dialog = Dialog(videoListActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.video_list_menu_dialog)
        val isSelectAll = adapter.getSelectedVideoSize() == adapter.getVideoListSize()

        val sort = dialog.findViewById<CardView>(R.id.sort_by_card)
        val select = dialog.findViewById<CardView>(R.id.de_select_all_card)
        val rename = dialog.findViewById<CardView>(R.id.rename_card)
        val delete = dialog.findViewById<CardView>(R.id.delete_card)

        val selectText = dialog.findViewById<TextView>(R.id.de_select_all_text)

        selectText.text = if (isSelectAll) videoListActivity.applicationContext.getString(R.string.deselect_all) else videoListActivity.applicationContext.getString(
            R.string.select_all
        )
        // Получение видео которое выделено
        vm.editableVideo = adapter.getEditableVideo()
        // Если выбрано 1 видео и оно имеет статус "загружено" - отображается кнопка возможности переименовать видео
        if (adapter.getSelectedVideoSize() == 1 && (vm.editableVideo?.loadingType ?: false) == VideoLoadingType.DONE)
            rename.visibility = View.VISIBLE
        else
            rename.visibility = View.GONE
        // Если видео нет - кнопка "select" скрывается
        select.visibility = if (adapter.getVideoListSize() == 0) View.GONE else View.VISIBLE

        sort.setOnClickListener {
            openSortByMenu()
            dialog.dismiss()
        }

        select.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                /*
                if (isSelectAll)
                    vm.deSelectVideo(selectAllMode = false)
                else
                    vm.deSelectVideo(selectAllMode = true) */
                if (isSelectAll)
                    adapter.deselectAll()
                else
                    adapter.selectAll()
                dialog.dismiss()
            }
        })

        rename.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                openRenameMenu()
                dialog.dismiss()
            }
        })

        delete.setOnClickListener {
            vm.deleteSelectedVideo(selectedVideos = adapter.getSelectedVideo())
            dialog.dismiss()
        }

        dialog.show()
        if (dialog.window != null) {
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setGravity(Gravity.BOTTOM)
        }
    }
 */

    private fun openRenameMenu() {
        val renameMenu = Dialog(videoListActivity)
        renameMenu.requestWindowFeature(Window.FEATURE_NO_TITLE)
        renameMenu.setContentView(R.layout.video_list_menu_rename_dialog)

        val editText = renameMenu.findViewById<EditText>(R.id.video_name_edittext)
        editText.setText(vm.editableVideo?.name)

        editText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                textView.clearFocus()
                val video = vm.videoList.value?.find { it.id == vm.editableVideo?.id }?.copy()

                if (video == null) {
                    // Если видео не найдено - его нельзя редактировать
                    Toast.makeText(context, getString(R.string.the_video_does_not_exist), Toast.LENGTH_SHORT).show()
                } else if (video.loadingType != VideoLoadingType.DONE) {
                    // Если видео не загружено - его нельзя редактировать
                    Toast.makeText(context, getString(R.string.the_video_is_uploading), Toast.LENGTH_SHORT).show()
                } else {
                    // Обновляем и загружаем видео
                    video.apply {
                        name = textView.text.toString()
                        isSelected = false
                    }
                    vm.editVideo(video = video)
                }

                adapter.clearSelection()
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

        nameCheckBox.isChecked = vm.getVideoOrder() is VideoOrder.Name
        dateCheckBox.isChecked = vm.getVideoOrder() is VideoOrder.Date
        durationCheckBox.isChecked = vm.getVideoOrder() is VideoOrder.Duration

        val sortType = vm.getVideoOrder()
        setButtonColors(ascending = sortType.orderType is OrderType.Ascending)

        ascendingButton.setOnClickListener {
            setButtonColors(ascending = true)
            vm.setOrderType(newOrderType = OrderType.Ascending)
            val currentVideoOrder = vm.getVideoOrder()
            vm.setVideoOrder(currentVideoOrder)
        }

        descendingButton.setOnClickListener {
            setButtonColors(ascending = false)
            vm.setOrderType(newOrderType = OrderType.Descending)
            val currentVideoOrder = vm.getVideoOrder()
            vm.setVideoOrder(currentVideoOrder)
        }

        nameCardView.setOnClickListener {
            val currentOrderType = vm.getOrderType()
            vm.setVideoOrder(VideoOrder.Name(currentOrderType))
        }
        dateCardView.setOnClickListener {
            val currentOrderType = vm.getOrderType()
            vm.setVideoOrder(VideoOrder.Date(currentOrderType))
        }
        durationCardView.setOnClickListener {
            val currentOrderType = vm.getOrderType()
            vm.setVideoOrder(VideoOrder.Duration(currentOrderType))
        }

        clearButton.setOnClickListener {
            vm.setVideoOrder(VideoListViewModel.DEFAULT_SORT_MODE)
        }
        applyButton.setOnClickListener {
            vm.updateVideoList()
            sortByDialog.dismiss()
        }

        vm.videoOrder.observe(videoListActivity) { sortMode ->
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
        adapter.setOnModeChangeListener(this@VideoListFragment)
    }

    override fun onModeChange(isNormalMode: Boolean) {
        if (!::searchEditText.isInitialized && !::searchLayout.isInitialized && !::bottomNav.isInitialized && !::editListLayout.isInitialized && !::uploadVideoCard.isInitialized) return
        val visibleInNormalMode = if (isNormalMode) View.VISIBLE else View.GONE
        val visibleInSelectionMode = if (!isNormalMode) View.VISIBLE else View.GONE

        searchEditText.isEnabled = isNormalMode
        searchEditText.isClickable = isNormalMode
        searchLayout.alpha = if (isNormalMode) 1f else 0.4f

        closeSelectionButton.visibility = visibleInSelectionMode

        bottomNav.visibility = visibleInNormalMode
        editListLayout.visibility = visibleInSelectionMode
        uploadVideoCard.visibility = visibleInNormalMode
    }

    override fun onSelectAll(isSelectAll: Boolean) {
        if (!::deSelectAllMenuText.isInitialized && !::deleteMenu.isInitialized) return
        // Если выделены все видео - текст убрать выделение. И наоборот
        deSelectAllMenuText.text = if (isSelectAll)
            videoListActivity.applicationContext.getString(R.string.deselect_all)
        else
            videoListActivity.applicationContext.getString(R.string.select_all)

        // Если не выделено ни одного видео кнопка удаление скрывается
        if (adapter.getSelectedVideoSize() == 0) {
            deleteMenu.alpha = 0.4f
            deleteMenu.isClickable = false
            deleteMenu.isFocusable = false
        } else {
            deleteMenu.alpha = 1f
            deleteMenu.isClickable = true
            deleteMenu.isFocusable = true
        }
    }

    // Если выделено только 1 видео
    override fun onSingleSelected(isSingleSelected: Boolean) {
        if (!::renameMenu.isInitialized) return
        renameMenu.alpha = if (isSingleSelected) 1f else 0.4f
        renameMenu.isClickable = isSingleSelected
        renameMenu.isFocusable = isSingleSelected
    }

}