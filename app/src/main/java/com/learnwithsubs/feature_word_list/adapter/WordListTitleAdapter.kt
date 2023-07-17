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

    private var wordsList = ArrayList<WordTranslation>()
    private var selectedWordsList = ArrayList<WordTranslation>()
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
        itemSelectedList.clear(); selectedWordsList.clear()
        for (item in groupedList) {
            val newWordTranslationWithTitle = WordTranslationWithTitle(item.id, item.title, ArrayList())
            itemSelectedList.add(newWordTranslationWithTitle)
        }

        val diffResult = DiffUtil.calculateDiff(GenericDiffCallback(itemList, groupedList))
        itemList = ArrayList(groupedList)
        wordsList = ArrayList(newItemList)
        diffResult.dispatchUpdatesTo(this@WordListTitleAdapter)
        callbacks()
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

    fun updateSelection(parentPosition: Int, position: Int, isSelected: Boolean) {
        val word = itemList.getOrNull(parentPosition)?.listWords?.getOrNull(position) ?: return
        if (isSelected) {
            itemSelectedList[parentPosition].listWords.add(word)
            selectedWordsList.add(word)
        }
        else {
            itemSelectedList[parentPosition].listWords.removeIf { it.id == word.id }
            selectedWordsList.removeIf { it.id == word.id }
        }
        val isChecked = itemList[parentPosition].listWords.size == itemSelectedList[parentPosition].listWords.size
        onSelectParentChangeListener?.onParentChange(position = parentPosition, isChecked = isChecked)

        callbacks()
    }

    fun selectAll() {
        val newList = itemList.map { it.copy(listWords = ArrayList(it.listWords)) }
        itemSelectedList = ArrayList(newList)
        selectedWordsList = ArrayList(wordsList)
        notifyDataSetChanged()
        callbacks()
    }
    fun selectAllChild(position: Int) {
        val title = itemList.getOrNull(position) ?: return
        selectedWordsList.addAll(title.listWords.filter { word -> !selectedWordsList.any { it.id == word.id } })
        itemSelectedList[position].listWords = ArrayList(title.listWords)
        callbacks()
    }
    fun deselectAllChild(position: Int) {
        val title = itemSelectedList.getOrNull(position) ?: return
        selectedWordsList.removeIf { word -> title.listWords.any { it.id == word.id } }
        title.listWords.clear()
        callbacks()
    }

    fun deselectAll() {
        selectedWordsList.clear()
        itemSelectedList.forEach { it.listWords.clear() }
        notifyDataSetChanged()
        callbacks()
    }

    fun changeMode(isSelectionMode: Boolean) {
        this.isSelectionMode = isSelectionMode
        if (!isSelectionMode) {
            selectedWordsList.clear()
            for (elem in itemSelectedList)
                elem.listWords.clear()
        }
        notifyDataSetChanged()
        callbacks()
    }


    private fun callbacks() {
        val itemList = wordsList
        val selectedItems = selectedWordsList

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
        return wordsList.size // convert(itemList).size
    }
    fun getChildSelectedItemsSize(): Int {
        return selectedWordsList.size //convert(itemSelectedList).size
    }
    fun getChildSelectedItems(): List<WordTranslation> {
        return selectedWordsList //convert(itemSelectedList)
    }
    fun getEditableItem(): WordTranslation? {
        val selected = selectedWordsList // convert(itemSelectedList)
        return if (selected.size == 1) selected.find { it.id == selected[0].id } //videoSelected[0].copy()
        else null
    }
    fun getIsSelectionMode(): Boolean {
        return isSelectionMode
    }
    /*
    private fun convert(toConvert: List<WordTranslationWithTitle>) : List<WordTranslation> {
        val result = ArrayList<WordTranslation>()
        for (item in toConvert) {
            result.addAll(item.listWords)
        }
        return result
    }
     */


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