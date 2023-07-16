package com.learnwithsubs.feature_word_list.adapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.databinding.ParentItemsBinding
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
    private val binding = ParentItemsBinding.bind(itemView)

    override fun bind(word: WordTranslationWithTitle, isSelected: Boolean) {
        binding.title.text = word.title
        binding.selectCheckBox.isChecked = isSelected
        val subAdapterItems = WordListAdapter(ArrayList(word.listWords), adapter.getIsSelectionMode())
        binding.recyclerWords.layoutManager = LinearLayoutManager(itemView.context)
        binding.recyclerWords.adapter = subAdapterItems

        itemView.setOnClickListener (object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (adapter.getIsSelectionMode()) {
                    val position = adapterPosition
                    val isChecked = binding.selectCheckBox.isChecked
                    if (isChecked)
                        adapter.deselectAllChild(position)
                    else
                        adapter.selectAllChild(position)
                    binding.recyclerWords.adapter = subAdapterItems
                    binding.selectCheckBox.isChecked = isChecked == false
                } else {
                    binding.recyclerWords.visibility =
                        if (binding.recyclerWords.isShown) View.GONE else View.VISIBLE
                }
            }
        })

        if (adapter.getIsSelectionMode()) {
            binding.selectCheckBox.visibility = View.VISIBLE
            binding.expandButton.visibility = View.GONE
        } else {
            binding.selectCheckBox.visibility = View.GONE
            binding.expandButton.visibility = View.VISIBLE
        }

        /*
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