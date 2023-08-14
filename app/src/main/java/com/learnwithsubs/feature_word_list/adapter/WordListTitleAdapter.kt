package com.learnwithsubs.feature_word_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.OnSelectChange
import com.learnwithsubs.R
import com.learnwithsubs.feature_word_list.model.WordList
import com.learnwithsubs.feature_word_list.model.WordTitle
import com.learnwithsubs.feature_word_list.model.WordManager
import com.learnwithsubs.feature_word_list.models.WordTranslation

class WordListTitleAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemList = ArrayList<WordList>()

    private var items = WordManager()
    private var selectedItems = WordManager()

    private var isSelectionMode = false

    private var onSelectChangeListener: OnSelectChange? = null
    private var prevIsSelectionMode = false
    private var prevIsSelectOne = false
    private var prevIsSelectAll = false

    fun setListener(onSelectChangeListener: OnSelectChange) {
        this.onSelectChangeListener = onSelectChangeListener
    }

    fun updateData(newItemList: List<WordTranslation>) {
        val groupedList = ArrayList(sortAndAddTitles(newItemList))
        clearSelectionList()

        val diffResult = DiffUtil.calculateDiff(GenericDiffCallback(itemList, groupedList))
        itemList = ArrayList(groupedList)
        diffResult.dispatchUpdatesTo(this@WordListTitleAdapter)
        callbacks()
    }

    private fun sortAndAddTitles(list: List<WordTranslation>): List<WordList> {
        val groupedMap = list.groupBy { it.videoID }

        items = WordManager()
        selectedItems = WordManager()
        for ((videoID, translations) in groupedMap) {
            val videoName = translations.firstOrNull()?.videoName
            items.addTitle(WordTitle(videoID?.times(-1), videoName, ArrayList(translations)))
            selectedItems.addTitle(WordTitle(videoID?.times(-1), videoName, ArrayList()))
        }

        val resultList = mutableListOf<WordList>()
        for (data in items.getList()) {
            val elem = data.wordList.getOrNull(0)
            resultList.add(WordList.Title(data.id, elem?.videoName, elem?.videoID))
            for (word in data.wordList) {
                resultList.add(WordList.Data(word.id, word))
            }
        }

        return resultList
    }
    
    private fun dimensional1To2(goal: Int): Int {
        var index = 0
        for ((id, elem) in items.getList().withIndex()) {
            index += elem.wordList.size + 1
            if (index > goal)
                return id
        }
        return items.getListSize() - 1
    }

    private fun getParentTileId(Xindex: Int): Int {
        var index = 0
        val list = items.getList()
        for (i in 0 until Xindex) {
            index += list[i].wordList.size + 1
        }
        return index
    }

    fun updateSelection(position: Int, isSelected: Boolean) {
        val word = itemList.getOrNull(position) as? WordList.Data ?: return

        val index = dimensional1To2(goal = position)
        if (isSelected)
            selectedItems.addWord(elem = word.data, index = index)
        else
            selectedItems.removeWord(elem = word.data, index = index)

        val pos = getParentTileId(Xindex = index)
        notifyItemChanged(pos)

        callbacks()
    }

    fun selectAll() {
        selectedItems.setData(list = items.getList())
        notifyDataSetChanged()
        callbacks()
    }
    fun selectAllChild(position: Int) {
        val index = dimensional1To2(goal = position)
        val tile = items.getTitle(index)
        selectedItems.updateTitle(elem = tile, index = index)

        notifyItemRangeChanged(position, selectedItems.getTitle(index).wordList.size + 1)
        callbacks()
    }
    fun deselectAllChild(position: Int) {
        val index = dimensional1To2(goal = position)
        selectedItems.removeTitle(index = index)

        notifyItemRangeChanged(position, items.getTitle(index).wordList.size + 1)
        callbacks()
    }
    fun deselectAll() {
        clearSelectionList()
        notifyDataSetChanged()
        callbacks()
    }

    fun changeMode(isSelectionMode: Boolean) {
        this.isSelectionMode = isSelectionMode
        if (!isSelectionMode)
            clearSelectionList()
        notifyDataSetChanged()
        callbacks()
    }

    private fun clearSelectionList() {
        selectedItems.clearWords()
    }

    private fun callbacks() {
        val itemList = items.getWordsList()
        val selectedList = selectedItems.getWordsList()

        if (itemList.isEmpty() && selectedList.isEmpty()) {
            isSelectionMode = false
        }

        if (prevIsSelectionMode != isSelectionMode)
            onSelectChangeListener?.onModeChange(isSelectionMode)

        prevIsSelectionMode = isSelectionMode

        if (!isSelectionMode) return

        if (selectedList.isEmpty())
            onSelectChangeListener?.onZeroSelect()
        else if (itemList.size == selectedList.size)
            onSelectChangeListener?.onSelectAll()
        else if (prevIsSelectAll)
            onSelectChangeListener?.onDeselectAll()
        else
            onSelectChangeListener?.onSomeSelect()

        prevIsSelectAll = itemList.size == selectedList.size

        if (selectedList.size == 1)
            onSelectChangeListener?.onSingleSelected()
        else if (prevIsSelectOne)
            onSelectChangeListener?.onNotSingleSelected()

        prevIsSelectOne = selectedList.size == 1
    }

    fun getChildItemListSize(): Int {
        return items.getWordsListSize()
    }
    fun getChildSelectedItemsSize(): Int {
        return selectedItems.getWordsListSize()
    }
    fun getChildSelectedItems(): List<WordTranslation> {
        val selected = ArrayList<WordTranslation>()
        for (elem in selectedItems.getWordsList()) {
            selected.add(elem)
        }
        return selected
    }
    fun getEditableItem(): WordTranslation? {
        val selected = selectedItems.getWordsList()
        return if (selected.size == 1) {
            return selected.find { it.id == selected[0].id }
        } else null
    }
    fun getIsSelectionMode(): Boolean {
        return isSelectionMode
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> WordTitleViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.tile_word_title, parent, false), this@WordListTitleAdapter)

            2 -> WordWithTranslationViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.tile_word_with_translation, parent, false), this@WordListTitleAdapter)

            else -> throw IllegalArgumentException("Unexpected viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val elem = itemList[position]) {
            is WordList.Title ->  {
                val normalHolder = holder as WordTitleViewHolder
                val id = dimensional1To2(goal = position)
                val isSelected = items.getTitleSize(id) == selectedItems.getTitleSize(id)
                val title = itemList[position] as WordList.Title
                normalHolder.bind(title.title, isSelected)
            }
            is WordList.Data -> {
                val normalHolder = holder as WordWithTranslationViewHolder
                val isSelected = selectedItems.getWordsList().any { it.id == elem.id }
                val data = itemList[position] as WordList.Data
                normalHolder.bind(data.data, isSelected)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (itemList[position]) {
            is WordList.Title -> 1
            is WordList.Data -> 2
        }
    }


    inner class GenericDiffCallback(private val oldList: List<WordList>, private val newList: List<WordList>) : DiffUtil.Callback() {
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