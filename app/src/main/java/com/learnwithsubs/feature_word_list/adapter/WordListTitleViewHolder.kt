package com.learnwithsubs.feature_word_list.adapter

import android.speech.tts.TextToSpeech
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.databinding.ParentItemsBinding
import com.learnwithsubs.databinding.TileWordTitleBinding
import com.learnwithsubs.databinding.TileWordWithTranslationBinding
import com.learnwithsubs.feature_word_list.model.WordTranslationWithTitle
import com.learnwithsubs.feature_word_list.models.WordTranslation
import okhttp3.internal.notify
import java.util.Locale

// Родительский класс для всех видео ViewHolder
abstract class WordListTitleViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(word: WordTranslationWithTitle)
}

// ViewHolder для обычного видео
class WordTitleViewHolder(
    itemView: View,
    private val adapter: WordListTitleAdapter
) : WordListTitleViewHolder(itemView) {
    private val binding = ParentItemsBinding.bind(itemView)

    override fun bind(word: WordTranslationWithTitle) {
        binding.title.text = word.title
        val subAdapterItems = WordListAdapter(ArrayList(word.listWords))
        binding.recyclerWords.layoutManager = LinearLayoutManager(itemView.context)
        binding.recyclerWords.adapter = subAdapterItems

        itemView.setOnClickListener (object : View.OnClickListener {
            override fun onClick(p0: View?) {
                itemView.visibility = View.VISIBLE //TODO
            }
        })

        /*
        if (adapter.getIsSelectionMode()) {
            binding.selectCheckBox.visibility = View.VISIBLE
            binding.audioOutputWord.visibility = View.GONE
        } else {
            binding.selectCheckBox.visibility = View.GONE
            binding.audioOutputWord.visibility = View.VISIBLE
        }

        itemView.setOnClickListener (object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (adapter.getIsSelectionMode()) {
                    val position = adapterPosition
                    binding.selectCheckBox.isChecked = !binding.selectCheckBox.isChecked
                    adapter.updateSelection(position = position, isSelected = binding.selectCheckBox.isChecked)
                }
            }
        })

        itemView.setOnLongClickListener {
            val position = adapterPosition
            binding.selectCheckBox.isChecked = !binding.selectCheckBox.isChecked
            adapter.updateSelection(position = position, isSelected = binding.selectCheckBox.isChecked)
            if (!adapter.getIsSelectionMode())
                adapter.changeMode(isSelectionMode = true)
            true
        }
         */

    }
}