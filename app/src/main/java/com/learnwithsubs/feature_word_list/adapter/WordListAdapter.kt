package com.learnwithsubs.feature_word_list.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.R
import com.learnwithsubs.feature_video_list.adapter.LoadingVideoViewHolder
import com.learnwithsubs.feature_video_list.adapter.NormalVideoViewHolder
import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.models.VideoStatus

class WordListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val wordList: ArrayList<Video> = ArrayList()
    private val selectedWordList: ArrayList<Video> = ArrayList()




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

        }
    }

    override fun getItemCount(): Int {
        return wordList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val video = wordList[position]

        when (video.videoStatus) {
            val normalHolder = holder as
            val isSelected = selectedWordList.any { it.id == wordList[position].id }
            normalHolder.bind(wordList[position], isSelected)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return wordList[position].videoStatus.value
    }
}