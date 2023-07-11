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

    init {
        val arrL = ArrayList<WordTranslation>()
        val item1 = WordTranslation(id = 0, word = "aaaalo", translation = "аааало", nativeLanguage = "ru", learnLanguage = "en")
        val item2 = WordTranslation(id = 0, word = "hello", translation = "привет", nativeLanguage = "ru", learnLanguage = "en")
        val item3 = WordTranslation(id = 0, word = "world", translation = "мир", nativeLanguage = "ru", learnLanguage = "en")
        arrL.add(item1)
        arrL.add(item2)
        arrL.add(item3)
        itemList = arrL
    }

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

    class RecyclerViewItemDecoration(private val spaceSize: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.left = spaceSize
            outRect.right = spaceSize
            outRect.bottom = spaceSize
            outRect.top = spaceSize
        }
    }
}