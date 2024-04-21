package com.learnwithsubs.video_view.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.video_view.R
import com.learnwithsubs.video_view.databinding.TileTranslatePartspeechBinding
import com.learnwithsubs.video_view.domain.models.DictionaryElement

class DictionaryPartSpeechHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    private val binding = TileTranslatePartspeechBinding.bind(itemView)

    fun bind(word: DictionaryElement.PartSpeech) {
        var ps = word.partSpeech
        when (word.partSpeech) {
            "noun" -> ps = itemView.context.getString(R.string.noun)
            "adjective" -> ps = itemView.context.getString(R.string.adjective)
            "adverb" -> ps = itemView.context.getString(R.string.adverb)
            "participle" -> ps = itemView.context.getString(R.string.participle)
            "predicative" -> ps = itemView.context.getString(R.string.predicative)
        }
        binding.partSpeech.text = ps
    }
}