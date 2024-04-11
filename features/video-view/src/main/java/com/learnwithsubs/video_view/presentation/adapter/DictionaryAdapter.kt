package com.learnwithsubs.video_view.presentation.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.video_view.R
import com.learnwithsubs.video_view.domain.models.DictionaryElement

class DictionaryAdapter(
    wordsInit: ArrayList<DictionaryElement>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var wordsList: ArrayList<DictionaryElement> = wordsInit
    private var onItemClickListener: OnDictionaryClick? = null


    fun updateData(wordsList: ArrayList<DictionaryElement>) {
        this@DictionaryAdapter.wordsList = wordsList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnDictionaryClick) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            2 -> DictionarySynonymsHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.tile_translate_word, parent, false), onItemClickListener)

            1 -> DictionaryPartSpeechHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.tile_translate_partspeech, parent, false))

            else -> throw IllegalArgumentException("Unexpected viewType: $viewType")
        }

    }

    override fun getItemCount(): Int {
        return wordsList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (val word = wordsList[position]) {
            is DictionaryElement.Synonyms -> {
                val synonymsHolder = holder as DictionarySynonymsHolder
                val syn = word as DictionaryElement.Synonyms
                synonymsHolder.bind(syn)
            }
            is DictionaryElement.PartSpeech -> {
                val partSpeechHolder = holder as DictionaryPartSpeechHolder
                val ps = word as DictionaryElement.PartSpeech
                partSpeechHolder.bind(ps)
            }
            /*DictionaryType.WORD -> {
                val normalHolder = holder as DictionaryNormalHolder
                normalHolder.bind(wordsList[position])
            }

            DictionaryType.PART_SPEECH -> {
                val loadingHolder = holder as DictionaryPartSpeechHolder
                loadingHolder.bind(wordsList[position])
            }*/
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (wordsList[position]) {
            is DictionaryElement.Synonyms -> 2
            is DictionaryElement.PartSpeech -> 1
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