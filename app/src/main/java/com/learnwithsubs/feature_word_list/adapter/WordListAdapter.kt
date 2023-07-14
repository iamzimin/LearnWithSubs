package com.learnwithsubs.feature_word_list.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.R
import com.learnwithsubs.SelectableAdapter
import com.learnwithsubs.feature_word_list.models.WordTranslation

class WordListAdapter: SelectableAdapter<WordTranslation>() {
    companion object {
        const val HEADER_VIEW_TYPE = 0
        const val ITEM_VIEW_TYPE = 1
    }

    override fun updateData(newItemList: List<WordTranslation>) {
        val newSortedItemList =
            newItemList.sortedWith(compareBy<WordTranslation> { it.videoID == null }
                .thenBy { it.videoID })
        val updatedList = updateList(sortedList = newSortedItemList)
        super.updateData(updatedList)
    }

    private fun updateList(sortedList: List<WordTranslation>) : List<WordTranslation> {
        val updatedList = mutableListOf<WordTranslation>()
        var previousVideoID: Int? = null
        for (wordTranslation in sortedList) {
            if (wordTranslation.videoID != previousVideoID) {
                val header = wordTranslation.copy().apply { isHeader = true }
                updatedList.add(header)
                previousVideoID = wordTranslation.videoID
            }
            updatedList.add(wordTranslation)
        }
        return updatedList
    }

    /*
    private fun groupDataByVideoName(data: List<WordTranslation>): List<Pair<Int?,String?>> {
        val uniquePairs = data
            .distinctBy { Pair(it.videoID, it.videoName) }  // Отобрать уникальные пары по значениям videoID и videoName
            .map { Pair(it.videoID, it.videoName) }         // Преобразовать элементы в пары videoID и videoName
        return uniquePairs
    }
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE -> {
                WordWithTranslationViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.tile_word_with_translation, parent, false), this@WordListAdapter)
            }
            HEADER_VIEW_TYPE -> {
                WordTitleViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.tile_word_title, parent, false), this@WordListAdapter)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val word = itemList[position]

        if (word.isHeader) {
            val titleHolder = holder as WordTitleViewHolder
            val isSelected = selectedItems.any { it.id == word.id }
            titleHolder.bind(word, isSelected)
        } else {
            val normalHolder = holder as WordWithTranslationViewHolder
            val isSelected = selectedItems.any { it.id == word.id }
            normalHolder.bind(word, isSelected)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val elem = itemList[position]
        return if (elem.isHeader) {
            HEADER_VIEW_TYPE
        } else {
            ITEM_VIEW_TYPE
        }
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