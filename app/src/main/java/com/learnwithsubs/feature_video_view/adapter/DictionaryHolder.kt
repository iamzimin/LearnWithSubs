package com.learnwithsubs.feature_video_view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.databinding.TranslatePartspeechTileBinding
import com.learnwithsubs.databinding.TranslateWordTileBinding
import com.learnwithsubs.feature_video_view.models.DictionarySynonyms

abstract class DictionaryHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(word: DictionarySynonyms)
}

class DictionaryNormalHolder(
    itemView: View,
    private val listener: OnDictionaryClick?
) : DictionaryHolder(itemView) {
    private val binding = TranslateWordTileBinding.bind(itemView)

    override fun bind(word: DictionarySynonyms) {
        binding.wordId.text = word.id.toString()
        binding.similarWord.text = word.word
        binding.similarWordTranslate.text = word.translation

        itemView.setOnClickListener (object : View.OnClickListener {
            override fun onClick(p0: View?) {
                listener?.onItemClick(
                    similarWord = binding.similarWord.text.toString(),
                    similarWordTranslate = binding.similarWordTranslate.text.toString()
                )
            }
        })
    }
}

class DictionaryPartSpeechHolder(
    itemView: View
) : DictionaryHolder(itemView) {
    private val binding = TranslatePartspeechTileBinding.bind(itemView)

    override fun bind(word: DictionarySynonyms) {
        binding.partSpeech.text = word.partSpeech
    }
}
