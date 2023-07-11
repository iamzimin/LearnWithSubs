package com.learnwithsubs.feature_word_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.R
import com.learnwithsubs.SelectableAdapter
import com.learnwithsubs.feature_word_list.models.WordTranslation

class WordListAdapter: SelectableAdapter<WordTranslation>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WordWithTranslationViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.tile_word_with_translation, parent, false), this@WordListAdapter
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val word = itemList[position]

        val normalHolder = holder as WordWithTranslationViewHolder
        val isSelected = selectedItems.any { it.id == word.id }
        normalHolder.bind(word, isSelected)
    }
}