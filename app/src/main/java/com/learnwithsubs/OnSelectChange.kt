package com.learnwithsubs

interface OnSelectChange {
    fun onModeChange(isSelectionMode: Boolean)
    fun onSelectAll()
    fun onDeselectAll()
    fun onZeroSelect()
    fun onSingleSelected()
    fun onNotSingleSelected()
}