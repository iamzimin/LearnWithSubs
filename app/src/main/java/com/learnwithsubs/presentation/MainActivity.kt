package com.learnwithsubs.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.learnwithsubs.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_list)
        supportActionBar?.hide()
    }
}