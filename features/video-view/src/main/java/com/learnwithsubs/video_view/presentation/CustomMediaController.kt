package com.learnwithsubs.video_view.presentation

import android.content.Context
import android.view.View
import android.widget.MediaController


class CustomMediaController(context: Context) : MediaController(context) {
    override fun setAnchorView(view: View) {}
}