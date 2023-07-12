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
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.learnwithsubs.OnSelectChange
import com.learnwithsubs.R
import com.learnwithsubs.databinding.ActivityVideoListBinding
import com.learnwithsubs.databinding.DialogVideoListMenuRenameBinding
import com.learnwithsubs.databinding.DialogVideoListMenuSortByBinding
import com.learnwithsubs.databinding.FragmentVideoListBinding
import com.learnwithsubs.databinding.SearchViewBinding
import com.learnwithsubs.general.util.OrderType
import com.learnwithsubs.feature_video_list.util.VideoOrder
import com.learnwithsubs.feature_video_list.adapter.VideoListAdapter
import com.learnwithsubs.feature_video_list.models.VideoErrorType
import com.learnwithsubs.feature_video_list.models.VideoLoadingType
import com.learnwithsubs.feature_video_list.videos.VideoListViewModel
import com.learnwithsubs.feature_video_list.videos.VideoListViewModelFactory


class VideoListFragment : Fragment(), OnSelectChange {
    companion object {
        const val PICK_VIDEO_REQUEST = 1
        const val PICK_SUBTITLES_REQUEST = 2
    }

    private lateinit var vmFactory: VideoListViewModelFactory
    private lateinit var vm: VideoListViewModel
    private lateinit var videoListActivity: VideoListActivity

    private lateinit var videoListVideoPicker: VideoListVideoPicker
    private lateinit var videoListSubtitlePicker: VideoListSubtitlePicker

    private lateinit var videoListFragmentBinding: FragmentVideoListBinding
    private lateinit var searchViewBinding: SearchViewBinding
    private lateinit var renameDialogBinding: DialogVideoListMenuRenameBinding
    private lateinit var videoListBinding: ActivityVideoListBinding
    private lateinit var sortByDialogBinding: DialogVideoListMenuSortByBinding

    private val adapter = VideoListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        videoListActivity = requireActivity() as VideoListActivity
        videoListFragmentBinding = FragmentVideoListBinding.inflate(inflater, container, false)
        sortByDialogBinding = DialogVideoListMenuSortByBinding.inflate(videoListActivity.layoutInflater)
        renameDialogBinding = DialogVideoListMenuRenameBinding.inflate(videoListActivity.layoutInflater)
        videoListBinding = videoListActivity.videoListBinding
        searchViewBinding = videoListFragmentBinding.searchBar
        setupRecyclerView()

        vmFactory = videoListActivity.vmFactory
        vm = ViewModelProvider(videoListActivity, vmFactory)[VideoListViewModel::class.java]

        videoListVideoPicker = VideoListVideoPicker(this, PICK_VIDEO_REQUEST)
        videoListSubtitlePicker = VideoListSubtitlePicker(this, PICK_SUBTITLES_REQUEST)


        (videoListFragmentBinding.videoList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        videoListFragmentBinding.loadVideoCard.setOnClickListener {
            videoListVideoPicker.pickVideo()
        }

        videoListFragmentBinding.sortBy.setOnClickListener {
            openSortByMenu()
        }
        videoListFragmentBinding.closeSelectionMode.setOnClickListener{
            adapter.changeMode(isSelectionMode = false) //adapter.clearSelection()
        }
        searchViewBinding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!adapter.getIsSelectionMode()) return
                vm.setFilterMode(filter = s.toString())
                vm.updateVideoList()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        searchViewBinding.searchEditText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                textView.clearFocus()
                true
            } else false
        }

        videoListFragmentBinding.deSelectAllMenu.setOnClickListener {
            val isSelectAll = adapter.getSelectedItemsSize() == adapter.getItemListSize()
            if (isSelectAll)
                adapter.deselectAll()
            else
                adapter.selectAll()
        }
        videoListFragmentBinding.renameMenu.setOnClickListener {
            vm.editableVideo = adapter.getEditableItem()
            openRenameMenu()
        }
        videoListFragmentBinding.deleteMenu.setOnClickListener {
            vm.deleteSelectedVideo(selectedVideos = adapter.getSelectedItems())
            adapter.changeMode(isSelectionMode = false)
        }
        videoListFragmentBinding.addSubtitlesMenu.setOnClickListener {
            vm.editableVideo = adapter.getEditableItem()
            val video = vm.editableVideo ?: return@setOnClickListener
            if (!video.isOwnSubtitles)
                videoListSubtitlePicker.pickSubtitles()
            else
                vm.backOldSubtitles(video = video)
            adapter.changeMode(isSelectionMode = false)
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
                adapter.updateItem(videoProgress.copy())
        }

        return videoListFragmentBinding.root
    }


    private fun openRenameMenu() {
        val renameMenu = Dialog(videoListActivity)
        renameMenu.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogView = renameDialogBinding.root
        val parentView = dialogView.parent as? ViewGroup
        parentView?.removeView(dialogView)
        renameMenu.setContentView(dialogView)

        fun save(textView: TextView) {
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
                video.name = textView.text.toString()
                vm.editVideo(video = video)
            }

            adapter.changeMode(isSelectionMode = false)
            renameMenu.dismiss()
            vm.editableVideo = null
        }

        renameDialogBinding.videoNameEdittext.setText(vm.editableVideo?.name)

        renameDialogBinding.videoNameEdittext.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                save(textView)
                true
            } else false
        }
        renameDialogBinding.save.setOnClickListener {
            save(renameDialogBinding.videoNameEdittext)
        }

        renameMenu.show()
        renameMenu.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        renameMenu.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun openSortByMenu() {
        val sortByDialog = Dialog(videoListActivity)
        sortByDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogView = sortByDialogBinding.root
        val parentView = dialogView.parent as? ViewGroup
        parentView?.removeView(dialogView)
        sortByDialog.setContentView(dialogView)

        fun setButtonColors(ascending: Boolean) {
            sortByDialogBinding.ascending.setBackgroundColor(if (ascending) videoListActivity.applicationContext.getColor(
                R.color.button_pressed
            ) else videoListActivity.applicationContext.getColor(R.color.button_normal))
            sortByDialogBinding.descending.setBackgroundColor(if (ascending) videoListActivity.applicationContext.getColor(
                R.color.button_normal
            ) else videoListActivity.applicationContext.getColor(R.color.button_pressed))
        }

        sortByDialogBinding.nameCheckBox.isChecked = vm.getVideoOrder() is VideoOrder.Name
        sortByDialogBinding.dateCheckBox.isChecked = vm.getVideoOrder() is VideoOrder.Date
        sortByDialogBinding.durationCheckBox.isChecked = vm.getVideoOrder() is VideoOrder.Duration

        val sortType = vm.getVideoOrder()
        setButtonColors(ascending = sortType.orderType is OrderType.Ascending)

        sortByDialogBinding.ascending.setOnClickListener {
            setButtonColors(ascending = true)
            vm.setOrderType(newOrderType = OrderType.Ascending)
            val currentVideoOrder = vm.getVideoOrder()
            vm.setVideoOrder(currentVideoOrder)
        }

        sortByDialogBinding.descending.setOnClickListener {
            setButtonColors(ascending = false)
            vm.setOrderType(newOrderType = OrderType.Descending)
            val currentVideoOrder = vm.getVideoOrder()
            vm.setVideoOrder(currentVideoOrder)
        }

        sortByDialogBinding.cardViewName.setOnClickListener {
            val currentOrderType = vm.getOrderType()
            vm.setVideoOrder(VideoOrder.Name(currentOrderType))
        }
        sortByDialogBinding.cardViewDate.setOnClickListener {
            val currentOrderType = vm.getOrderType()
            vm.setVideoOrder(VideoOrder.Date(currentOrderType))
        }
        sortByDialogBinding.cardViewDuration.setOnClickListener {
            val currentOrderType = vm.getOrderType()
            vm.setVideoOrder(VideoOrder.Duration(currentOrderType))
        }

        sortByDialogBinding.clearButton.setOnClickListener {
            vm.setVideoOrder(VideoListViewModel.DEFAULT_SORT_MODE)
        }
        sortByDialogBinding.applyButton.setOnClickListener {
            vm.updateVideoList()
            sortByDialog.dismiss()
        }

        vm.videoOrder.observe(videoListActivity) { sortMode ->
            sortByDialogBinding.nameCheckBox.isChecked = sortMode is VideoOrder.Name
            sortByDialogBinding.dateCheckBox.isChecked = sortMode is VideoOrder.Date
            sortByDialogBinding.durationCheckBox.isChecked = sortMode is VideoOrder.Duration
        }

        sortByDialog.show()
        sortByDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        sortByDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        sortByDialog.window?.setGravity(Gravity.BOTTOM)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        videoListVideoPicker.loadVideoOnResult(requestCode, resultCode, data, vm, videoListActivity)
        videoListSubtitlePicker.loadVideoOnResult(requestCode, resultCode, data, vm, vm.editableVideo)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(videoListActivity)
        videoListFragmentBinding.videoList.layoutManager = layoutManager
        videoListFragmentBinding.videoList.adapter = adapter
        val itemDecoration = VideoListAdapter.RecyclerViewItemDecoration(16)
        videoListFragmentBinding.videoList.addItemDecoration(itemDecoration)
        adapter.setOnModeChangeListener(this@VideoListFragment)
    }

    private fun changeCardVisibility(cardView: CardView, isVisible: Boolean) {
        cardView.alpha = if (isVisible) 1f else 0.4f
        cardView.isClickable = isVisible
        cardView.isFocusable = isVisible
    }

    private fun setTextInSubtitleMenu() {
        val editVideo = adapter.getEditableItem()
        if (editVideo != null)
            videoListFragmentBinding.addSubtitlesMenuText.text = if (editVideo.isOwnSubtitles) getString(R.string.return_subtitles) else getString(R.string.add_subtitles)
    }

    override fun onModeChange(isSelectionMode: Boolean) {
        val visibleInNormalMode = if (!isSelectionMode) View.VISIBLE else View.GONE
        val visibleInSelectionMode = if (isSelectionMode) View.VISIBLE else View.GONE

        if (isSelectionMode) setTextInSubtitleMenu()

        searchViewBinding.searchEditText.isEnabled = !isSelectionMode
        searchViewBinding.searchEditText.isClickable = !isSelectionMode
        searchViewBinding.root.alpha = if (!isSelectionMode) 1f else 0.6f

        videoListFragmentBinding.closeSelectionMode.visibility = visibleInSelectionMode

        videoListBinding.fragmentNavigation.visibility = visibleInNormalMode
        videoListFragmentBinding.editListLayout.visibility = visibleInSelectionMode
        videoListFragmentBinding.loadVideoCard.visibility = visibleInNormalMode
    }


    // Если выделено всё
    override fun onSelectAll() {
        videoListFragmentBinding.deSelectAllMenuText.text = videoListActivity.applicationContext.getString(R.string.deselect_all)
        changeCardVisibility(cardView = videoListFragmentBinding.deleteMenu, isVisible = true)
    }

    // Если было выделено всё, а стало на 1 и более меньше
    override fun onDeselectAll() {
        videoListFragmentBinding.deSelectAllMenuText.text = videoListActivity.applicationContext.getString(R.string.select_all)
    }

    // Если не выделено ничего
    override fun onZeroSelect() {
        videoListFragmentBinding.deSelectAllMenuText.text = videoListActivity.applicationContext.getString(R.string.select_all)

        changeCardVisibility(cardView = videoListFragmentBinding.deleteMenu, isVisible = false)
        changeCardVisibility(cardView = videoListFragmentBinding.renameMenu, isVisible = false)
        changeCardVisibility(cardView = videoListFragmentBinding.addSubtitlesMenu, isVisible = false)
    }

    // Если выделено только 1 видео
    override fun onSingleSelected() {
        setTextInSubtitleMenu()
        changeCardVisibility(cardView = videoListFragmentBinding.deleteMenu, isVisible = true)
        if ((adapter.getEditableItem()?.loadingType ?: false) == VideoLoadingType.DONE) {
            changeCardVisibility(cardView = videoListFragmentBinding.addSubtitlesMenu, isVisible = true)
            changeCardVisibility(cardView = videoListFragmentBinding.renameMenu, isVisible = true)
        } else {
            changeCardVisibility(cardView = videoListFragmentBinding.addSubtitlesMenu, isVisible = false)
            changeCardVisibility(cardView = videoListFragmentBinding.renameMenu, isVisible = false)
        }
    }

    // Если выделено было выделено 1 видео, а стало любое другое число
    override fun onNotSingleSelected() {
        setTextInSubtitleMenu()
        changeCardVisibility(cardView = videoListFragmentBinding.addSubtitlesMenu, isVisible = false)
        changeCardVisibility(cardView = videoListFragmentBinding.renameMenu, isVisible = false)
    }

}