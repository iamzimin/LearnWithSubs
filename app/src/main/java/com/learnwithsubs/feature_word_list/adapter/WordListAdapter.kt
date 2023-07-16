package com.learnwithsubs.feature_word_list.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.R
import com.learnwithsubs.SelectableAdapter
import com.learnwithsubs.feature_word_list.models.WordTranslation

class WordListAdapter(
    override var itemList: ArrayList<WordTranslation>,
    isSelectMode: Boolean
): SelectableAdapter<WordTranslation>(itemList) {
    private var parentAdapter: WordListTitleAdapter? = null

    init {
        if (isSelectMode) super.changeMode(isSelectionMode = true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var parentView: View? = parent.parent as View
        while (parentView != null && parentView !is RecyclerView) {
            parentView = parentView.parent as? View
        }
        val parentRecyclerView = parentView as RecyclerView
        parentAdapter = parentRecyclerView.adapter as WordListTitleAdapter
        val nonNullParentAdapter = parentAdapter ?: throw IllegalArgumentException("Parent adapter is null") //TODO

        return WordWithTranslationViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.tile_word_with_translation, parent, false),
            nonNullParentAdapter
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val word = itemList[position]

        val normalHolder = holder as WordWithTranslationViewHolder
        val nonNullParentAdapter = parentAdapter ?: throw IllegalArgumentException("Parent adapter is null") //TODO
        val isSelected = nonNullParentAdapter.getChildSelectedItems().any { it.id == word.id }
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