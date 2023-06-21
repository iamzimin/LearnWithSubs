package com.learnwithsubs.feature_video_view

import android.content.Context
import android.view.View
import android.widget.ImageButton
import android.widget.MediaController
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.learnwithsubs.R


class CustomMediaController(context: Context) : MediaController(context) {
    override fun setAnchorView(view: View) {}
}