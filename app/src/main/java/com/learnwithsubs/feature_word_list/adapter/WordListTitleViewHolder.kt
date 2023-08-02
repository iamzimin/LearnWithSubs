package com.learnwithsubs.feature_word_list.adapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.databinding.ParentItemBinding
import com.learnwithsubs.feature_word_list.model.WordTranslationWithTitle

abstract class WordListTitleViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(word: WordTranslationWithTitle, isSelected: Boolean)
}

class WordTitleViewHolder(
    itemView: View,
    private val adapter: WordListTitleAdapter
) : WordListTitleViewHolder(itemView) {
    private val binding = ParentItemBinding.bind(itemView)

    override fun bind(word: WordTranslationWithTitle, isSelected: Boolean) {
        val parentPos = adapterPosition
        binding.title.text = word.title
        binding.selectCheckBox.isChecked = isSelected
        val subAdapterItems = WordListAdapter(ArrayList(word.listWords), adapter, parentPos)
        binding.recyclerWords.layoutManager = LinearLayoutManager(itemView.context)
        binding.recyclerWords.adapter = subAdapterItems

        fun updateSelectionChild(position: Int, isChecked: Boolean) {
            if (isChecked)
                adapter.deselectAllChild(position)
            else
                adapter.selectAllChild(position)
            binding.recyclerWords.adapter = subAdapterItems //TODO
            binding.selectCheckBox.isChecked = isChecked == false
        }

        if (adapter.getIsSelectionMode()) {
            binding.selectCheckBox.visibility = View.VISIBLE
            binding.expandButton.visibility = View.GONE
        } else {
            binding.selectCheckBox.visibility = View.GONE
            binding.expandButton.visibility = View.VISIBLE
        }

        itemView.setOnClickListener (object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val position = adapterPosition
                if (adapter.getIsSelectionMode()) {
                    updateSelectionChild(position = position, isChecked = binding.selectCheckBox.isChecked)
                } else {
                    binding.recyclerWords.visibility = if (binding.recyclerWords.isShown) View.GONE else View.VISIBLE
                }
            }
        })

        itemView.setOnLongClickListener {
            val position = adapterPosition
            if (!adapter.getIsSelectionMode())
                adapter.changeMode(isSelectionMode = true)
            updateSelectionChild(position = position, isChecked = binding.selectCheckBox.isChecked)
            true
        }

    }
}