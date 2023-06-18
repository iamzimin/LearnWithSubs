package com.learnwithsubs.feature_video_view.presentation.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.R
import com.learnwithsubs.feature_video_view.domain.models.DictionaryWord

class DictionaryAdapter(
    wordsInit: ArrayList<DictionaryWord>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var wordsList: ArrayList<DictionaryWord> = wordsInit


    fun updateData(wordsList: ArrayList<DictionaryWord>) {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DictionaryNormalHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.translated_tile, parent, false))
    }

    override fun getItemCount(): Int {
        return wordsList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val loadingHolder = holder as DictionaryNormalHolder
        loadingHolder.bind(wordsList[position])
    }

//    override fun getItemViewType(position: Int): Int {
//        return wordsList[position].videoStatus.value
//    }

    class RecyclerViewItemDecoration(private val spaceSize: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.left = spaceSize
            outRect.right = spaceSize
            outRect.bottom = spaceSize
            outRect.top = spaceSize
        }
    }
}