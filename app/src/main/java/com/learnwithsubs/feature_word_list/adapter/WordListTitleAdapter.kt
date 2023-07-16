package com.learnwithsubs.feature_word_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.OnSelectChange
import com.learnwithsubs.OnSelectParentChange
import com.learnwithsubs.R
import com.learnwithsubs.feature_word_list.model.WordTranslationWithTitle
import com.learnwithsubs.feature_word_list.models.WordTranslation

class WordListTitleAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemList = ArrayList<WordTranslationWithTitle>()
    private var itemSelectedList  = ArrayList<WordTranslationWithTitle>()
    private var isSelectionMode = false

    private var onSelectChangeListener: OnSelectChange? = null
    private var onSelectParentChangeListener: OnSelectParentChange? = null
    private var prevIsSelectionMode = false
    private var prevIsSelectOne = false
    private var prevIsSelectAll = false

    fun setListener(onSelectChange: OnSelectChange, onSelectParentChange: OnSelectParentChange) {
        onSelectChangeListener = onSelectChange
        onSelectParentChangeListener = onSelectParentChange
    }

    fun updateData(newItemList: List<WordTranslation>) {
        val groupedList = ArrayList(groupWordTranslationsByVideo(newItemList))
        itemSelectedList.clear()
        for (item in groupedList) {
            val newWordTranslationWithTitle = WordTranslationWithTitle(item.id, item.title, ArrayList())
            itemSelectedList.add(newWordTranslationWithTitle.copy())
        }

        val diffResult = DiffUtil.calculateDiff(GenericDiffCallback(itemList, groupedList))
        itemList = ArrayList(groupedList)
        diffResult.dispatchUpdatesTo(this@WordListTitleAdapter)
        callbacks(groupedList)
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

    fun updateSelection(position: Int, id: Int?, isSelected: Boolean) {
        var parentPos: Int? = null
        for ((index, item) in itemList.withIndex()) {
                val word = item.listWords.getOrNull(position)
                if (word != null && word.id == id) {
                    if (isSelected)
                        itemSelectedList[index].listWords.add(word)
                    else
                        itemSelectedList[index].listWords.remove(itemSelectedList[index].listWords.find { it.id == word.id })
                    parentPos = index
                    break
                }
        }
        if (parentPos != null) {
            val t = itemList[parentPos].listWords.size == itemSelectedList[parentPos].listWords.size
            onSelectParentChangeListener?.onParentChange(position = parentPos, isSelected = t)
        }
        callbacks(itemList)
    }

    fun selectAll() {
        itemSelectedList = ArrayList(itemList)
        changeMode(isSelectionMode = true)
    }
    fun selectAllChild(position: Int) {
        val title = itemList.getOrNull(position) ?: return
        itemSelectedList[position].listWords = ArrayList(title.listWords)
        callbacks(this.itemList)
    }
    fun deselectAllChild(position: Int) {
        val title = itemSelectedList.getOrNull(position) ?: return
        title.listWords.clear()
        callbacks(this.itemList)
    }

    fun deselectAll() {
        for (elem in itemSelectedList)
            elem.listWords.clear()
        notifyDataSetChanged()
        callbacks(this.itemList)
    }

    fun changeMode(isSelectionMode: Boolean) {
        this.isSelectionMode = isSelectionMode
        if (!isSelectionMode) {
            for (elem in itemSelectedList)
                elem.listWords.clear()
        }
        notifyDataSetChanged()
        callbacks(this.itemList)
    }


    private fun callbacks(list: List<WordTranslationWithTitle>) {
        val itemList = convert(list)
        val selectedItems = convert(itemSelectedList)

        if (itemList.isEmpty() && selectedItems.isEmpty()) {
            isSelectionMode = false
        }

        if (prevIsSelectionMode != isSelectionMode)
            onSelectChangeListener?.onModeChange(isSelectionMode)

        prevIsSelectionMode = isSelectionMode

        if (!isSelectionMode) return

        if (selectedItems.isEmpty())
            onSelectChangeListener?.onZeroSelect()
        else if (itemList.size == selectedItems.size)
            onSelectChangeListener?.onSelectAll()
        else if (prevIsSelectAll)
            onSelectChangeListener?.onDeselectAll()
        else
            onSelectChangeListener?.onSomeSelect()

        prevIsSelectAll = itemList.size == selectedItems.size

        if (selectedItems.size == 1)
            onSelectChangeListener?.onSingleSelected()
        else if (prevIsSelectOne)
            onSelectChangeListener?.onNotSingleSelected()

        prevIsSelectOne = selectedItems.size == 1
    }

    fun getChildItemListSize(): Int {
        return convert(itemList).size
    }
    fun getChildSelectedItemsSize(): Int {
        return convert(itemSelectedList).size
    }
    fun getChildSelectedItems(): List<WordTranslation> {
        return convert(itemSelectedList)
    }
    fun getEditableItem(): WordTranslation? {
        val selected = convert(itemSelectedList)
        return if (selected.size == 1) selected.find { it.id == selected[0].id } //videoSelected[0].copy()
        else null
    }
    fun getIsSelectionMode(): Boolean {
        return isSelectionMode
    }
    private fun convert(toConvert: List<WordTranslationWithTitle>) : List<WordTranslation> {
        val result = ArrayList<WordTranslation>()
        for (item in toConvert) {
            result.addAll(item.listWords)
        }
        return result
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WordTitleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.parent_items, parent, false), this@WordListTitleAdapter
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val word = itemList[position]

        val normalHolder = holder as WordTitleViewHolder
        val isSelected = itemList[position].listWords.size == itemSelectedList[position].listWords.size
        normalHolder.bind(word, isSelected)
    }

    override fun getItemCount(): Int {
        return itemList.size
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