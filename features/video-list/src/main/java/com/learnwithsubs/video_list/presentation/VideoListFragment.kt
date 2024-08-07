package com.learnwithsubs.video_list.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.base.OnSelectChange
import com.example.base.OnSelectionModeChange
import com.learnwithsubs.resource.R
import com.learnwithsubs.resource.databinding.SearchViewBinding
import com.learnwithsubs.video_list.databinding.FragmentVideoListBinding
import com.learnwithsubs.video_list.di.DaggerVideoListAppComponent
import com.learnwithsubs.video_list.di.VideoListAppModule
import com.learnwithsubs.video_list.domain.models.VideoErrorType
import com.learnwithsubs.video_list.domain.models.VideoLoadingType
import com.learnwithsubs.video_list.presentation.adapter.VideoListAdapter
import com.learnwithsubs.video_list.presentation.videos.VideoListViewModel
import com.learnwithsubs.video_list.presentation.videos.VideoListViewModelFactory
import javax.inject.Inject


class VideoListFragment : Fragment(), OnSelectChange {
    companion object {
        const val PICK_VIDEO_REQUEST = 1
        const val PICK_SUBTITLES_REQUEST = 2
    }
    @Inject
    lateinit var vmFactory: VideoListViewModelFactory
    private lateinit var vm: VideoListViewModel

    private lateinit var videoListVideoPicker: VideoListVideoPicker
    private lateinit var videoListSubtitlePicker: VideoListSubtitlePicker

    private lateinit var videoListFragmentBinding: FragmentVideoListBinding
    private lateinit var searchViewBinding: SearchViewBinding

    private val appContext: Context by lazy { requireContext() }

    private val adapter = VideoListAdapter(ArrayList())
    private var selectionMode: OnSelectionModeChange? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        DaggerVideoListAppComponent.builder().videoListAppModule(VideoListAppModule(context = appContext)).build().inject(this)
        vm = ViewModelProvider(this, vmFactory)[VideoListViewModel::class.java]

        videoListFragmentBinding = FragmentVideoListBinding.inflate(inflater, container, false)
        searchViewBinding = videoListFragmentBinding.searchBar
        val sortByDialog = SortByDialog(fragment = this@VideoListFragment, vm = vm, adapter = adapter)
        val renameDialog = RenameDialog(fragment = this@VideoListFragment, vm = vm, adapter = adapter)
        setupRecyclerView()


        videoListVideoPicker = VideoListVideoPicker(this, PICK_VIDEO_REQUEST)
        videoListSubtitlePicker = VideoListSubtitlePicker(this, PICK_SUBTITLES_REQUEST)


        videoListFragmentBinding.loadVideoCard.setOnClickListener {
            videoListVideoPicker.pickVideo()
        }

        videoListFragmentBinding.sortBy.setOnClickListener {
            sortByDialog.openSortByMenu()
        }
        videoListFragmentBinding.closeSelectionMode.setOnClickListener{
            adapter.changeMode(isSelectionMode = false)
        }
        searchViewBinding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (adapter.getIsSelectionMode()) return
                vm.setFilterMode(filter = s.toString())
                val data = vm.videoList.value ?: return
                adapter.updateData(ArrayList(vm.getSortedVideoList(data)))
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
            renameDialog.openRenameMenu()
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
        vm.errorTypeLiveData.observe(viewLifecycleOwner) { video ->
            if (video == null) return@observe
            val errorType = video.errorType ?: return@observe
            when (errorType) {
                VideoErrorType.PROCESSING_VIDEO ->      Toast.makeText(this@VideoListFragment.appContext, getString(
                    R.string.video_processing_error), Toast.LENGTH_SHORT).show()
                VideoErrorType.EXTRACTING_AUDIO ->      Toast.makeText(this@VideoListFragment.appContext, getString(R.string.audio_extraction_error), Toast.LENGTH_SHORT).show()
                VideoErrorType.DECODING_VIDEO ->        Toast.makeText(this@VideoListFragment.appContext, getString(R.string.video_decoding_error), Toast.LENGTH_SHORT).show()
                VideoErrorType.GENERATING_SUBTITLES ->  Toast.makeText(this@VideoListFragment.appContext, getString(R.string.subtitle_generation_error), Toast.LENGTH_SHORT).show()
                VideoErrorType.UPLOADING_AUDIO ->       Toast.makeText(this@VideoListFragment.appContext, getString(R.string.audio_upload_error), Toast.LENGTH_SHORT).show()
            }
            vm.deleteVideo(video = video)
        }

        vm.videoList.observe(viewLifecycleOwner) { videoList ->
            videoList ?: return@observe
            adapter.updateData(ArrayList(videoList))
        }

        vm.videoProgressLiveData.observe(viewLifecycleOwner) { videoProgress ->
            if (videoProgress != null)
                adapter.updateVideoProgress(videoId = videoProgress.first, uploadingProgress = videoProgress.second)
        }

        return videoListFragmentBinding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        videoListVideoPicker.loadVideoOnResult(requestCode, resultCode, data, vm, appContext)
        videoListSubtitlePicker.loadVideoOnResult(requestCode, resultCode, data, vm, vm.editableVideo)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(appContext)
        videoListFragmentBinding.videoList.layoutManager = layoutManager
        videoListFragmentBinding.videoList.adapter = adapter
        val itemDecoration = VideoListAdapter.RecyclerViewItemDecoration(16)
        videoListFragmentBinding.videoList.addItemDecoration(itemDecoration)
        adapter.setOnModeChangeListener(this@VideoListFragment)
        (videoListFragmentBinding.videoList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
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

        selectionMode?.selectionModeChange(isSelectionMode = isSelectionMode)
        videoListFragmentBinding.editListLayout.visibility = visibleInSelectionMode
        videoListFragmentBinding.loadVideoCard.visibility = visibleInNormalMode
    }


    // If all is selected
    override fun onSelectAll() {
        videoListFragmentBinding.deSelectAllMenuText.text = appContext.getString(R.string.deselect_all)
        changeCardVisibility(cardView = videoListFragmentBinding.deleteMenu, isVisible = true)
    }

    // If everything was allocated, but it became 1 or more less
    override fun onDeselectAll() {
        videoListFragmentBinding.deSelectAllMenuText.text = appContext.getString(R.string.select_all)
    }

    // If nothing is highlighted
    override fun onZeroSelect() {
        videoListFragmentBinding.deSelectAllMenuText.text = appContext.getString(R.string.select_all)

        changeCardVisibility(cardView = videoListFragmentBinding.deleteMenu, isVisible = false)
        changeCardVisibility(cardView = videoListFragmentBinding.renameMenu, isVisible = false)
        changeCardVisibility(cardView = videoListFragmentBinding.addSubtitlesMenu, isVisible = false)
    }

    // If only 1 video is highlighted
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

    // If 1 video was highlighted, but became any other number
    override fun onNotSingleSelected() {
        setTextInSubtitleMenu()
        changeCardVisibility(cardView = videoListFragmentBinding.addSubtitlesMenu, isVisible = false)
        changeCardVisibility(cardView = videoListFragmentBinding.renameMenu, isVisible = false)
    }

    // If more than 1 video is highlighted
    override fun onSomeSelect() {
        changeCardVisibility(cardView = videoListFragmentBinding.deleteMenu, isVisible = true)
    }

    fun attachSelectionMode(listener: OnSelectionModeChange) {
        this.selectionMode = listener
    }
}