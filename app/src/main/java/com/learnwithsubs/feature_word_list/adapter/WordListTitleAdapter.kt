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
    private var itemSelectedList = ArrayList<WordList>()

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
        itemSelectedList.clear()

        val diffResult = DiffUtil.calculateDiff(GenericDiffCallback(itemList, groupedList))
        itemList = ArrayList(groupedList)
        diffResult.dispatchUpdatesTo(this@WordListTitleAdapter)
        callbacks()
    }

    private fun sortAndAddTitles(list: List<WordTranslation>): List<WordList> {
        val sortedList = list.sortedBy { it.videoID }.reversed()
        val resultList = mutableListOf<WordList>()

        var prevVideoName: Int? = null
        for ((index, elem) in sortedList.withIndex()) {
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
            itemSelectedList.add(word)
        else
            itemSelectedList.removeIf { it.id == word.id }

        val originalCount = itemList.count { item ->
            if (item is WordList.Data) {
                item.data.videoName == word.data.videoName
            } else false
        }
        val selectedCount = itemSelectedList.count { item ->
            if (item is WordList.Data) {
                item.data.videoName == word.data.videoName
            } else false
        }

        var pos = position - 1
        while (pos > 0) {
            when (itemList[pos]) {
                is WordList.Data -> pos--
                is WordList.Title -> break
            }
        }

        if (originalCount == selectedCount) itemSelectedList.add(itemList[pos])
        else itemSelectedList.removeIf { it.id == itemList[pos].id }
        notifyItemChanged(pos)

        callbacks()
    }

    fun selectAll() {
        val copiedItemList = itemList.map {
            when (it) {
                is WordList.Title -> WordList.Title(it.id, it.title, it.videoID)
                is WordList.Data -> WordList.Data(it.id, it.data)
            }
        }.toMutableList()
        itemSelectedList = ArrayList(copiedItemList)
        notifyDataSetChanged()
        callbacks()
    }
    fun selectAllChild(position: Int) {
        itemSelectedList.add(itemList[position])

        var currentIndex = position + 1
        while (currentIndex < itemList.size) {
            when (val testData = itemList[currentIndex]) {
                is WordList.Data -> {
                    if (!itemSelectedList.any { it.id == testData.id })
                        itemSelectedList.add(testData)
                    currentIndex++
                }
                is WordList.Title -> break
            }
        }
        notifyItemRangeChanged(position, currentIndex)
        callbacks()
    }
    fun deselectAllChild(position: Int) {
        itemSelectedList.removeIf { it.id == itemList[position].id }

        var currentIndex = position + 1
        while (currentIndex < itemList.size) {
            when (val testData = itemList[currentIndex]) {
                is WordList.Data -> {
                    itemSelectedList.removeIf{ it.id == testData.id}
                    currentIndex++
                }
                is WordList.Title -> break
            }
        }
        notifyItemRangeChanged(position, currentIndex)
        callbacks()
    }

    fun deselectAll() {
        itemSelectedList.clear()
        notifyDataSetChanged()
        callbacks()
    }

    fun changeMode(isSelectionMode: Boolean) {
        this.isSelectionMode = isSelectionMode
        if (!isSelectionMode)
            itemSelectedList.clear()
        notifyDataSetChanged()
        callbacks()
    }


    private fun callbacks() {
        val itemList = itemList
        val selectedItems = itemSelectedList

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
        return itemSelectedList.size
    }
    fun getChildSelectedItems(): List<WordTranslation> {
        val selected = ArrayList<WordTranslation>()
        for (elem in itemSelectedList) {
            if (elem is WordList.Data)
                selected.add(elem.data)
        }
        return selected
    }
    fun getEditableItem(): WordTranslation? {
        val selected = itemSelectedList
        return if (selected.size == 1) {
            val test = selected.find { it.id == selected[0].id } as? WordList.Data
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
                val isSelected = itemSelectedList.any { it.id == elem.id }
                val title = itemList[position] as WordList.Title
                normalHolder.bind(title.title, isSelected)
            }
            is WordList.Data -> {
                val normalHolder = holder as WordWithTranslationViewHolder
                val isSelected = itemSelectedList.any { it.id == elem.id }
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