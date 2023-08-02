package com.learnwithsubs.feature_word_list.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.R
import com.learnwithsubs.SelectableAdapter
import com.learnwithsubs.feature_word_list.models.WordTranslation

class WordListAdapter(
    private var itemList: ArrayList<WordTranslation>,
    private val parentAdapter: WordListTitleAdapter,
    private val parentPosition: Int,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateData(list: List<WordTranslation>) {
        itemList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WordWithTranslationViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.tile_word_with_translation, parent, false),
            parentAdapter,
            parentPosition
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val word = itemList[position]
        if (itemList.size - 1 == position)
            Toast.makeText(holder.itemView.context, "All", Toast.LENGTH_SHORT).show()

        val normalHolder = holder as WordWithTranslationViewHolder
        val isSelected = parentAdapter.getChildSelectedItems().any { it.id == word.id }
        normalHolder.bind(word, isSelected)
    }

    class RecyclerViewItemDecoration(private val spaceSize: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.left = spaceSize
            outRect.right = spaceSize
            outRect.bottom = spaceSize
            outRect.top = spaceSize
        }
    }
}