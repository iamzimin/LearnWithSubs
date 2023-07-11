package com.learnwithsubs.feature_video_list.adapter

interface OnSelectChange {
    fun onModeChange(isSelectionMode: Boolean)
    fun onSelectAll()
    fun onDeselectAll()
    fun onZeroSelect()
    fun onSingleSelected()
    fun onNotSingleSelected()
}