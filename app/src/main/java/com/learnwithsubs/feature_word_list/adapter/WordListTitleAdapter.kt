package com.learnwithsubs.feature_word_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.R
import com.learnwithsubs.feature_word_list.model.WordTranslationWithTitle
import com.learnwithsubs.feature_word_list.models.WordTranslation

class WordListTitleAdapter(
    var itemList: ArrayList<WordTranslationWithTitle>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateData(newItemList: List<WordTranslation>) {
        itemList = ArrayList(groupWordTranslationsByVideo(newItemList))
        notifyDataSetChanged()
    }

    private fun groupWordTranslationsByVideo(list: List<WordTranslation>): List<WordTranslationWithTitle> {
        val groupedMap = list.groupBy { it.videoID }
        val result = mutableListOf<WordTranslationWithTitle>()

        groupedMap.forEach { (videoID, translations) ->
            val videoName = translations.firstOrNull()?.videoName ?: ""
            val wordTranslationWithTitle = WordTranslationWithTitle(videoName, translations)
            result.add(wordTranslationWithTitle)
        }

        return result
    }

    /*
    private fun updateList(sortedList: List<WordTranslation>) : List<WordTranslation> {
        val updatedList = mutableListOf<WordTranslationWithTitle>()
        val wordTransl = mutableListOf<WordTranslation>()
        var previousVideoID: Int? = null
        for (wordTranslation in sortedList) {
            val header = wordTranslation.copy()
            wordTransl.add(header)
            if (wordTranslation.videoID != previousVideoID) {
                updatedList.add(wordTranslation)
                previousVideoID = wordTranslation.videoID
            }
        }
        return updatedList
    }
     */


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WordTitleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.parent_items, parent, false), this@WordListTitleAdapter
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val word = itemList[position]

        val normalHolder = holder as WordTitleViewHolder
        normalHolder.bind(word)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}