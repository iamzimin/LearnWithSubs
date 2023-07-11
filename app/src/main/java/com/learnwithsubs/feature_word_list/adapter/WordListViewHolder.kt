package com.learnwithsubs.feature_word_list.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.databinding.TileWordWithTranslationBinding
import com.learnwithsubs.feature_word_list.models.WordTranslation

// Родительский класс для всех видео ViewHolder
abstract class WordListViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(word: WordTranslation, isSelected: Boolean)
}

// ViewHolder для обычного видео
class WordWithTranslationViewHolder(
    itemView: View,
    private val adapter: WordListAdapter
) : WordListViewHolder(itemView) {
    private val binding = TileWordWithTranslationBinding.bind(itemView)

    override fun bind(word: WordTranslation, isSelected: Boolean) {
        binding.word.setText(word.word)
        binding.translation.setText(word.translation)


        if (adapter.getIsSelectionMode())
            binding.selectCheckBox.visibility = View.VISIBLE
        else
            binding.selectCheckBox.visibility = View.GONE

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

    }
}