package com.learnwithsubs.video_view.presentation.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.R

class DictionaryAdapter(
    wordsInit: ArrayList<com.example.yandex_dictionary_api.models.DictionarySynonyms>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var wordsList: ArrayList<com.example.yandex_dictionary_api.models.DictionarySynonyms> = wordsInit
    private var onItemClickListener: OnDictionaryClick? = null


    fun updateData(wordsList: ArrayList<com.example.yandex_dictionary_api.models.DictionarySynonyms>) {
        this@DictionaryAdapter.wordsList = wordsList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnDictionaryClick) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            com.example.yandex_dictionary_api.models.DictionaryType.WORD.value -> DictionaryNormalHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.tile_translate_word, parent, false), onItemClickListener)

            com.example.yandex_dictionary_api.models.DictionaryType.PART_SPEECH.value -> DictionaryPartSpeechHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.tile_translate_partspeech, parent, false))

            else -> {
                DictionaryPartSpeechHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.tile_translate_partspeech, parent, false))
            }
        }

    }

    override fun getItemCount(): Int {
        return wordsList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val word = wordsList[position]

        when (word.type) {
            com.example.yandex_dictionary_api.models.DictionaryType.WORD -> {
                val normalHolder = holder as DictionaryNormalHolder
                normalHolder.bind(wordsList[position])
            }

            com.example.yandex_dictionary_api.models.DictionaryType.PART_SPEECH -> {
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