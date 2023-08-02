package com.learnwithsubs.feature_word_list.adapter

import android.speech.tts.TextToSpeech
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.databinding.TileWordTitleBinding
import com.learnwithsubs.databinding.TileWordWithTranslationBinding
import com.learnwithsubs.feature_word_list.model.WordTranslationWithTitle
import com.learnwithsubs.feature_word_list.models.WordTranslation
import java.util.Locale

abstract class WordListViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(word: WordTranslation, isSelected: Boolean)
}

class WordWithTranslationViewHolder(
    itemView: View,
    private val parentAdapter: WordListTitleAdapter,
    private val parentPosition: Int,
) : WordListViewHolder(itemView), TextToSpeech.OnInitListener {
    private val binding = TileWordWithTranslationBinding.bind(itemView)
    //private val tts: TextToSpeech = TextToSpeech(itemView.context, this)

    override fun bind(word: WordTranslation, isSelected: Boolean) {
        binding.word.text = word.word
        binding.translation.text = word.translation
        binding.selectCheckBox.isChecked = isSelected
        //tts.language = Locale(word.learnLanguage)


        if (parentAdapter.getIsSelectionMode()) {
            binding.selectCheckBox.visibility = View.VISIBLE
            binding.audioOutputWord.visibility = View.GONE
        } else {
            binding.selectCheckBox.visibility = View.GONE
            binding.audioOutputWord.visibility = View.VISIBLE
        }

        itemView.setOnClickListener (object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (parentAdapter.getIsSelectionMode()) {
                    val position = adapterPosition
                    binding.selectCheckBox.isChecked = !binding.selectCheckBox.isChecked
                    parentAdapter.updateSelection(parentPosition = parentPosition ,position = position, isSelected = binding.selectCheckBox.isChecked)
                }
            }
        })

        itemView.setOnLongClickListener {
            val position = adapterPosition
            if (!parentAdapter.getIsSelectionMode())
                parentAdapter.changeMode(isSelectionMode = true)

            binding.selectCheckBox.isChecked = !binding.selectCheckBox.isChecked
            parentAdapter.updateSelection(parentPosition = parentPosition, position = position, isSelected = binding.selectCheckBox.isChecked)
            true
        }

        binding.audioOutputWord.setOnClickListener {
            //tts.speak(binding.word.text, TextToSpeech.QUEUE_FLUSH, null, "")
        }

    }

    override fun onInit(status: Int) {}
}