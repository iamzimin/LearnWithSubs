package com.learnwithsubs.feature_video_view.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.databinding.TranslatedTileBinding
import com.learnwithsubs.feature_video_view.domain.models.DictionaryWord

abstract class DictionaryHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(word: DictionaryWord)
}

class DictionaryNormalHolder(
    itemView: View,
    private val listener: OnDictionaryClick?
) : DictionaryHolder(itemView) {
    private val binding = TranslatedTileBinding.bind(itemView)

    override fun bind(word: DictionaryWord) {
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
