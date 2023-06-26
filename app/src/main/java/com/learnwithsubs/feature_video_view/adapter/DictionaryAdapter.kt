package com.learnwithsubs.feature_video_view.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.R
import com.learnwithsubs.feature_video_view.models.DictionarySynonyms
import com.learnwithsubs.feature_video_view.models.DictionaryType

class DictionaryAdapter(
    wordsInit: ArrayList<DictionarySynonyms>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var wordsList: ArrayList<DictionarySynonyms> = wordsInit
    private var onItemClickListener: OnDictionaryClick? = null


    fun updateData(wordsList: ArrayList<DictionarySynonyms>) {
        this@DictionaryAdapter.wordsList = wordsList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnDictionaryClick) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            DictionaryType.WORD.value -> DictionaryNormalHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.translate_word_tile, parent, false), onItemClickListener)

            DictionaryType.PART_SPEECH.value -> DictionaryPartSpeechHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.translate_partspeech_tile, parent, false))

            else -> { //TODO
                DictionaryPartSpeechHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.translate_partspeech_tile, parent, false))
            }
        }
    }

    override fun getItemCount(): Int {
        return wordsList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val word = wordsList[position]

        when (word.type) {
            DictionaryType.WORD -> {
                val normalHolder = holder as DictionaryNormalHolder
                normalHolder.bind(wordsList[position])
            }

            DictionaryType.PART_SPEECH -> {
                val loadingHolder = holder as DictionaryPartSpeechHolder
                loadingHolder.bind(wordsList[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return wordsList[position].type.value
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