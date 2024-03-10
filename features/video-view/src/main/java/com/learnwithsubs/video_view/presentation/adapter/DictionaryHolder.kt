package com.learnwithsubs.video_view.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.databinding.TileTranslatePartspeechBinding
import com.learnwithsubs.databinding.TileTranslateWordBinding

abstract class DictionaryHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(word: com.learnwithsubs.video_view.domain.models.DictionarySynonyms)
}

class DictionaryNormalHolder(
    itemView: View,
    private val listener: OnDictionaryClick?
) : DictionaryHolder(itemView) {
    private val binding = TileTranslateWordBinding.bind(itemView)

    override fun bind(word: com.learnwithsubs.video_view.domain.models.DictionarySynonyms) {
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
    private val binding = TileTranslatePartspeechBinding.bind(itemView)

    override fun bind(word: com.learnwithsubs.video_view.domain.models.DictionarySynonyms) {
        binding.partSpeech.text = word.partSpeech
    }
}
