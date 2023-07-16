package com.learnwithsubs.feature_word_list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.OnSelectChange
import com.learnwithsubs.R
import com.learnwithsubs.SelectableAdapter
import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_word_list.model.WordTranslationWithTitle
import com.learnwithsubs.feature_word_list.models.WordTranslation
import com.learnwithsubs.general.models.Identifiable

class WordListTitleAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemWithTitleList: ArrayList<WordTranslationWithTitle> = ArrayList()
    private var itemList: ArrayList<WordTranslation> = ArrayList()
    private var selectedItems = ArrayList<WordTranslation>()
    private var isSelectionMode = false

    private var onSelectChangeListener: OnSelectChange? = null
    private var prevIsSelectionMode = false
    private var prevIsSelectOne = false
    private var prevIsSelectAll = false

    fun setOnModeChangeListener(listener: OnSelectChange) {
        onSelectChangeListener = listener
    }

    fun updateData(newItemList: List<WordTranslation>) {
        val groupedList = ArrayList(groupWordTranslationsByVideo(newItemList))

        val diffResult = DiffUtil.calculateDiff(GenericDiffCallback(itemWithTitleList, groupedList))
        itemList = ArrayList(newItemList)
        itemWithTitleList = ArrayList(groupedList)
        diffResult.dispatchUpdatesTo(this@WordListTitleAdapter)
        callbacks(newItemList)
    }

    /*
    fun updateSelected(newItemList: List<WordTranslationWithTitle>) {
        if (newItemList.isEmpty()) {
            changeMode(isSelectionMode = false)
        }
        val iterator = selectedItems.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            val found = newItemList.listWords.find { it.id == item.id }
            if (found != null) {
                val index = selectedItems.indexOfFirst { it.id == item.id }
                if (index != -1) selectedItems[index] = found
            } else {
                iterator.remove()
            }
        }
    }
     */
    /*
    fun updateItem(elemToUpdate: WordTranslation) {
        itemWithTitleList.forEachIndexed { index, list ->
            val position = list.listWords.indexOfFirst { it.id == elemToUpdate.id }
            if (position != -1) {
                list.listWords[position] = elemToUpdate
                notifyItemChanged(position)
            }
        }
    }
     */

    fun updateSelection(position: Int, id: Int?, isSelected: Boolean) {
        for (item in itemWithTitleList) {
                val word = item.listWords.getOrNull(position)
                if (word != null && word.id == id) {
                    if (isSelected)
                        selectedItems.add(word)
                    else
                        selectedItems.remove(selectedItems.find { it.id == word.id })
                }
        }
        callbacks(this.itemList)
    }

    fun selectAll() {
        selectedItems = ArrayList(itemList.toList())
        changeMode(isSelectionMode = true)
    }

    fun deselectAll() {
        selectedItems.clear()
        notifyDataSetChanged()
        callbacks(this.itemList)
    }

    fun changeMode(isSelectionMode: Boolean) {
        this.isSelectionMode = isSelectionMode
        if (!isSelectionMode) {
            selectedItems.clear()
        }
        notifyDataSetChanged()
        callbacks(this.itemList)
    }


    private fun callbacks(itemList: List<WordTranslation>) {
        if (itemList.isEmpty() && selectedItems.isEmpty()) {
            isSelectionMode = false
        }

        if (prevIsSelectionMode != isSelectionMode)
            onSelectChangeListener?.onModeChange(isSelectionMode)

        prevIsSelectionMode = isSelectionMode

        if (!isSelectionMode) return

        if (selectedItems.size == 0)
            onSelectChangeListener?.onZeroSelect()
        else if (itemList.size == selectedItems.size)
            onSelectChangeListener?.onSelectAll()
        else if (prevIsSelectAll)
            onSelectChangeListener?.onDeselectAll()

        prevIsSelectAll = itemList.size == selectedItems.size

        if (selectedItems.size == 1)
            onSelectChangeListener?.onSingleSelected()
        else if (prevIsSelectOne)
            onSelectChangeListener?.onNotSingleSelected()

        prevIsSelectOne = selectedItems.size == 1
    }

    fun getItemListSize(): Int {
        return itemList.size
    }
    fun getSelectedItemsSize(): Int {
        return selectedItems.size
    }
    fun getSelectedItems(): List<WordTranslation> {
        return selectedItems
    }
    fun getEditableItem(): WordTranslation? {
        return if (selectedItems.size == 1) itemList.find { it.id == selectedItems[0].id } //videoSelected[0].copy()
        else null
    }
    fun getIsSelectionMode(): Boolean {
        return isSelectionMode
    }


    private fun groupWordTranslationsByVideo(list: List<WordTranslation>): List<WordTranslationWithTitle> {
        val groupedMap = list.groupBy { it.videoID }
        val result = mutableListOf<WordTranslationWithTitle>()

        var id = 0
        groupedMap.forEach { (videoID, translations) ->
            val videoName = translations.firstOrNull()?.videoName ?: "my"
            val wordTranslationWithTitle = WordTranslationWithTitle(id, videoName, ArrayList(translations))
            result.add(wordTranslationWithTitle)
            id++
        }

        return result
    }

    /*
    private fun updateList(sortedList: List<WordTranslation>) : List<WordTranslation> {
        val updatedList = mutableListOf<WordTranslationWithTitle>()
        val wordTransl = mutableListOf<WordTranslation>()
        var previousVideoID: Int? = null
        for (wordTranslation in sortedList) {
            val header = wordTranslation.copy()
            wordTransl.add(header)
            if (wordTranslation.videoID != previousVideoID) {
                updatedList.add(wordTranslation)
                previousVideoID = wordTranslation.videoID
            }
        }
        return updatedList
    }
     */


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WordTitleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.parent_items, parent, false), this@WordListTitleAdapter
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val word = itemWithTitleList[position]

        val normalHolder = holder as WordTitleViewHolder
        normalHolder.bind(word)
    }

    override fun getItemCount(): Int {
        return itemWithTitleList.size
    }


    inner class GenericDiffCallback(private val oldList: List<WordTranslationWithTitle>, private val newList: List<WordTranslationWithTitle>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}