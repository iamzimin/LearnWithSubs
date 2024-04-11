package com.learnwithsubs.video_view.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.video_view.databinding.TileTranslateWordBinding
import com.learnwithsubs.video_view.domain.models.DictionaryElement

class DictionarySynonymsHolder(
    itemView: View,
    private val listener: OnDictionaryClick?
) : RecyclerView.ViewHolder(itemView) {
    private val binding = TileTranslateWordBinding.bind(itemView)

    fun bind(word: DictionaryElement.Synonyms) {
        binding.wordId.text = word.dictionarySynonyms.id.toString()
        binding.similarWord.text = word.dictionarySynonyms.word
        binding.similarWordTranslate.text = word.dictionarySynonyms.translation

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