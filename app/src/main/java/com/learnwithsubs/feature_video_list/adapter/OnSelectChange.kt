package com.learnwithsubs.feature_video_list.adapter

interface OnSelectChange {
    fun onModeChange(isNormalMode: Boolean)
    fun onSelectAll(isSelectAll: Boolean)
    fun onSingleSelected(isSingleSelected: Boolean)
}