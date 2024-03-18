package com.learnwithsubs.video_list.presentation

interface OnSelectChange {
    fun onModeChange(isSelectionMode: Boolean)
    fun onSelectAll()
    fun onDeselectAll()
    fun onZeroSelect()
    fun onSingleSelected()
    fun onNotSingleSelected()
    fun onSomeSelect()
}