package com.learnwithsubs.feature_word_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnwithsubs.R
import com.learnwithsubs.app.App
import com.learnwithsubs.databinding.ActivityVideoListBinding
import com.learnwithsubs.databinding.FragmentWordListBinding
import com.learnwithsubs.databinding.SearchViewBinding
import com.learnwithsubs.feature_video_list.VideoListActivity
import com.learnwithsubs.feature_video_list.adapter.OnSelectChange
import com.learnwithsubs.feature_word_list.adapter.WordListAdapter
import javax.inject.Inject

class WordListFragment : Fragment(), OnSelectChange {
    @Inject
    lateinit var vmFactory: WordListViewModelFactory
    private lateinit var vm: WordListViewModel

    private lateinit var videoListActivity: VideoListActivity
    private lateinit var fragmentWordListBinding: FragmentWordListBinding
    private lateinit var searchViewBinding: SearchViewBinding
    private lateinit var videoListBinding: ActivityVideoListBinding


    private val adapter = WordListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        videoListActivity = requireActivity() as VideoListActivity
        fragmentWordListBinding = FragmentWordListBinding.inflate(inflater, container, false)
        searchViewBinding = fragmentWordListBinding.searchBar
        videoListBinding = videoListActivity.videoListBinding
        setupRecyclerView()

        (videoListActivity.applicationContext as App).wordListAppComponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)[WordListViewModel::class.java]

        vm.wordList.observe(videoListActivity) { wordList ->
            wordList ?: return@observe
            //val sorted = vm.getSortedVideoList(wordList = wordList)
            adapter.updateData(ArrayList(wordList.toList()))
        }

        fragmentWordListBinding.closeSelectionMode.setOnClickListener{
            adapter.changeMode(isSelectionMode = false)
        }
        fragmentWordListBinding.deSelectAllMenu.setOnClickListener {
            val isSelectAll = adapter.getSelectedItemsSize() == adapter.getItemListSize()
            if (isSelectAll)
                adapter.deselectAll()
            else
                adapter.selectAll()
        }
        fragmentWordListBinding.deleteMenu.setOnClickListener {
            vm.deleteWords(selectedWords = adapter.getSelectedItems())
        }

        return fragmentWordListBinding.root
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(videoListActivity)
        fragmentWordListBinding.wordList.layoutManager = layoutManager
        fragmentWordListBinding.wordList.adapter = adapter
        val itemDecoration = WordListAdapter.RecyclerViewItemDecoration(16)
        fragmentWordListBinding.wordList.addItemDecoration(itemDecoration)
        adapter.setOnModeChangeListener(this@WordListFragment)
    }



    private fun changeCardVisibility(cardView: CardView, isVisible: Boolean) {
        cardView.alpha = if (isVisible) 1f else 0.4f
        cardView.isClickable = isVisible
        cardView.isFocusable = isVisible
    }

    override fun onModeChange(isSelectionMode: Boolean) {
        val visibleInNormalMode = if (!isSelectionMode) View.VISIBLE else View.GONE
        val visibleInSelectionMode = if (isSelectionMode) View.VISIBLE else View.GONE

        searchViewBinding.searchEditText.isEnabled = !isSelectionMode
        searchViewBinding.searchEditText.isClickable = !isSelectionMode
        searchViewBinding.root.alpha = if (!isSelectionMode) 1f else 0.6f

        fragmentWordListBinding.closeSelectionMode.visibility = visibleInSelectionMode

        videoListBinding.fragmentNavigation.visibility = visibleInNormalMode
        fragmentWordListBinding.editListLayout.visibility = visibleInSelectionMode
        fragmentWordListBinding.addWordCard.visibility = visibleInNormalMode
    }


    // Если выделено всё
    override fun onSelectAll() {
        fragmentWordListBinding.deSelectAllMenuText.text = videoListActivity.applicationContext.getString(R.string.deselect_all)
        changeCardVisibility(cardView = fragmentWordListBinding.deleteMenu, isVisible = true)
    }

    // Если было выделено всё, а стало на 1 и более меньше
    override fun onDeselectAll() {
        fragmentWordListBinding.deSelectAllMenuText.text = videoListActivity.applicationContext.getString(R.string.select_all)
    }

    // Если не выделено ничего
    override fun onZeroSelect() {
        fragmentWordListBinding.deSelectAllMenuText.text = videoListActivity.applicationContext.getString(R.string.select_all)

        changeCardVisibility(cardView = fragmentWordListBinding.deleteMenu, isVisible = false)
        changeCardVisibility(cardView = fragmentWordListBinding.renameMenu, isVisible = false)
    }

    // Если выделено только 1 видео
    override fun onSingleSelected() {
        changeCardVisibility(cardView = fragmentWordListBinding.deleteMenu, isVisible = true)
        changeCardVisibility(cardView = fragmentWordListBinding.renameMenu, isVisible = true)
    }

    // Если выделено было выделено 1 видео, а стало любое другое число
    override fun onNotSingleSelected() {
        changeCardVisibility(cardView = fragmentWordListBinding.renameMenu, isVisible = false)
    }
}