package com.learnwithsubs.feature_word_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.OnSelectChange
import com.learnwithsubs.R
import com.learnwithsubs.feature_word_list.model.WordList
import com.learnwithsubs.feature_word_list.models.WordTranslation

class WordListTitleAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemList = ArrayList<WordList>()
    private var selectedTitle = ArrayList<WordList.Title>()
    private var selectedWord = ArrayList<WordList.Data>()

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
        val resultList = mutableListOf<WordList>()

        var prevVideoName: Int? = null
        for ((index, elem) in list.withIndex()) {
            if (elem.videoID != prevVideoName || index == 0) {
                val title = itemList.filterIsInstance<WordList.Title>().find { it.videoID == elem.videoID }
                if (title != null)
                    resultList.add(WordList.Title(title.id, elem.videoName ?: "custom", elem.videoID))
                else
                    resultList.add(WordList.Title(elem.id?.times(-1), elem.videoName ?: "custom", elem.videoID))

                prevVideoName = elem.videoID
            }
            resultList.add(WordList.Data(elem.id, elem))
        }

        return resultList
    }

    fun updateSelection(position: Int, isSelected: Boolean) {
        val word = itemList.getOrNull(position) as? WordList.Data ?: return

        if (isSelected)
            selectedWord.add(word)
        else
            selectedWord.removeIf { it.id == word.id }

        val originalCount = itemList.count { item ->
            if (item is WordList.Data) {
                item.data.videoID == word.data.videoID
            } else false
        }
        val selectedCount = selectedWord.count { item ->
            item.data.videoID == word.data.videoID
        }

        var pos = position - 1
        while (pos > 0) {
            when (itemList[pos]) {
                is WordList.Data -> pos--
                is WordList.Title -> break
            }
        }
        val title = itemList[pos] as? WordList.Title ?: return

        if (originalCount == selectedCount) selectedTitle.add(title)
        else selectedTitle.removeIf { it.id == title.id }
        notifyItemChanged(pos)

        callbacks()
    }

    fun selectAll() {
        val selectedT: MutableList<WordList.Title> = ArrayList()
        val selectedW: MutableList<WordList.Data> = ArrayList()
        itemList.forEach {
            when (it) {
                is WordList.Title -> selectedT.add(WordList.Title(it.id, it.title, it.videoID))
                is WordList.Data -> selectedW.add(WordList.Data(it.id, it.data))
            }
        }
        selectedTitle = ArrayList(selectedT)
        selectedWord = ArrayList(selectedW)
        notifyDataSetChanged()
        callbacks()
    }
    fun selectAllChild(position: Int) {
        val title = itemList[position] as? WordList.Title ?: return
        selectedTitle.add(title)

        var currentIndex = position + 1
        while (currentIndex < itemList.size) {
            when (val testData = itemList[currentIndex]) {
                is WordList.Data -> {
                    if (!selectedWord.any { it.id == testData.id })
                        selectedWord.add(testData)
                    currentIndex++
                }
                is WordList.Title -> break
            }
        }
        notifyItemRangeChanged(position, currentIndex)
        callbacks()
    }
    fun deselectAllChild(position: Int) {
        val title = itemList[position] as? WordList.Title ?: return
        selectedTitle.removeIf { it.id == title.id }

        var currentIndex = position + 1
        while (currentIndex < itemList.size) {
            when (val testData = itemList[currentIndex]) {
                is WordList.Data -> {
                    selectedWord.removeIf{ it.id == testData.id}
                    currentIndex++
                }
                is WordList.Title -> break
            }
        }
        notifyItemRangeChanged(position, currentIndex)
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
        selectedWord.clear(); selectedTitle.clear()
    }


    private fun callbacks() {
        val itemList = itemList
        val selectedItems = selectedWord

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
        return itemList.size
    }
    fun getChildSelectedItemsSize(): Int {
        return selectedWord.size
    }
    fun getChildSelectedItems(): List<WordTranslation> {
        val selected = ArrayList<WordTranslation>()
        for (elem in selectedWord) {
            selected.add(elem.data)
        }
        return selected
    }
    fun getEditableItem(): WordTranslation? {
        val selected = selectedWord
        return if (selected.size == 1) {
            val test = selected.find { it.id == selected[0].id }
            test?.data
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
                val isSelected = selectedTitle.any { it.id == elem.id }
                val title = itemList[position] as WordList.Title
                normalHolder.bind(title.title, isSelected)
            }
            is WordList.Data -> {
                val normalHolder = holder as WordWithTranslationViewHolder
                val isSelected = selectedWord.any { it.id == elem.id }
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