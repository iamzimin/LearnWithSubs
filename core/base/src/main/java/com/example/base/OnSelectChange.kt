package com.example.base

interface OnSelectChange {
    fun onModeChange(isSelectionMode: Boolean)
    fun onSelectAll()
    fun onDeselectAll()
    fun onZeroSelect()
    fun onSingleSelected()
    fun onNotSingleSelected()
    fun onSomeSelect()
}