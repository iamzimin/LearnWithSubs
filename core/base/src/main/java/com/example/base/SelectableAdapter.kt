package com.example.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class SelectableAdapter<T : Identifiable>(
    open var itemList: ArrayList<T>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected open var selectedItems = ArrayList<T>()
    private var isSelectionMode = false

    private var onSelectChangeListener: OnSelectChange? = null
    private var prevIsSelectionMode = false
    private var prevIsSelectOne = false
    private var prevIsSelectAll = false

    fun setOnModeChangeListener(listener: OnSelectChange) {
        onSelectChangeListener = listener
    }

    fun updateData(newItemList: List<T>) {
        val diffResult = DiffUtil.calculateDiff(GenericDiffCallback(itemList, newItemList))
        itemList = ArrayList(newItemList)
        diffResult.dispatchUpdatesTo(this@SelectableAdapter)
        updateSelected(newItemList)
        callbacks(newItemList)
    }

    protected open fun updateSelected(newItemList: List<T>) {
        if (newItemList.isEmpty()) {
            changeMode(isSelectionMode = false)
        }
        val iterator = selectedItems.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            val found = newItemList.find { it.id == item.id }
            if (found != null) {
                val index = selectedItems.indexOfFirst { it.id == item.id }
                if (index != -1) selectedItems[index] = found
            } else {
                iterator.remove()
            }
        }
    }

    fun updateItem(elemToUpdate: T) {
        val position = itemList.indexOfFirst { it.id == elemToUpdate.id }
        if (position != -1) {
            itemList[position] = elemToUpdate
            notifyItemChanged(position)
        }
    }

    fun updateSelection(position: Int, isSelected: Boolean) {
        itemList.getOrNull(position)?.let { item ->
            if (isSelected)
                selectedItems.add(item)
            else
                selectedItems.remove(selectedItems.find { it.id == item.id })
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

    private fun callbacks(itemList: List<T>) {
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
    fun getSelectedItems(): List<T> {
        return selectedItems
    }
    fun getEditableItem(): T? {
        return if (selectedItems.size == 1) itemList.find { it.id == selectedItems[0].id } //videoSelected[0].copy()
        else null
    }
    fun getIsSelectionMode(): Boolean {
        return isSelectionMode
    }


    class GenericDiffCallback<T : com.learnwithsubs.database.domain.Identifiable>(private val oldList: List<T>, private val newList: List<T>) : DiffUtil.Callback() {
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
