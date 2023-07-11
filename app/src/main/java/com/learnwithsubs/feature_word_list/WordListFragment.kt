package com.learnwithsubs.feature_word_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnwithsubs.R
import com.learnwithsubs.databinding.FragmentWordListBinding
import com.learnwithsubs.databinding.TileWordWithTranslationBinding
import com.learnwithsubs.feature_video_list.VideoListActivity
import com.learnwithsubs.feature_video_list.adapter.OnSelectChange
import com.learnwithsubs.feature_video_list.adapter.VideoListAdapter
import com.learnwithsubs.feature_word_list.adapter.WordListAdapter

class WordListFragment : Fragment(), OnSelectChange {
    private lateinit var videoListActivity: VideoListActivity
    private lateinit var fragmentWordListBinding: FragmentWordListBinding

    private val adapter = WordListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        videoListActivity = requireActivity() as VideoListActivity
        fragmentWordListBinding = FragmentWordListBinding.inflate(inflater, container, false)
        setupRecyclerView()




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






    override fun onModeChange(isSelectionMode: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onSelectAll() {
        TODO("Not yet implemented")
    }

    override fun onDeselectAll() {
        TODO("Not yet implemented")
    }

    override fun onZeroSelect() {
        TODO("Not yet implemented")
    }

    override fun onSingleSelected() {
        TODO("Not yet implemented")
    }

    override fun onNotSingleSelected() {
        TODO("Not yet implemented")
    }
}