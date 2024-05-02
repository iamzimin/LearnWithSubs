package com.learnwithsubs.word_list.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.resource.R
import com.learnwithsubs.word_list.databinding.TileWordTitleBinding

abstract class WordListTitleViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(title: String?, isSelected: Boolean)
}

class WordTitleViewHolder(
    itemView: View,
    private val adapter: WordListTitleAdapter
) : WordListTitleViewHolder(itemView) {
    private val binding = TileWordTitleBinding.bind(itemView)

    override fun bind(title: String?, isSelected: Boolean) {
        binding.title.text = title ?: itemView.context.getString(R.string.custom)
        binding.selectCheckBox.isChecked = isSelected

        fun updateSelectionChild(position: Int, isChecked: Boolean) {
            if (isChecked)
                adapter.deselectAllChild(position)
            else
                adapter.selectAllChild(position)
            binding.selectCheckBox.isChecked = isChecked == false
        }

        if (adapter.getIsSelectionMode())
            binding.selectCheckBox.visibility = View.VISIBLE
        else
            binding.selectCheckBox.visibility = View.GONE

        itemView.setOnClickListener (object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val position = adapterPosition
                if (adapter.getIsSelectionMode())
                    updateSelectionChild(position = position, isChecked = binding.selectCheckBox.isChecked)
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