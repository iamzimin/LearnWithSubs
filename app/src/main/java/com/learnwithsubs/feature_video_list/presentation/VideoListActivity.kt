package com.learnwithsubs.feature_video_list.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.learnwithsubs.ModuleListFragment
import com.learnwithsubs.R
import com.learnwithsubs.app.App
import com.learnwithsubs.feature_video_list.presentation.videos.VideoListViewModel
import com.learnwithsubs.feature_video_list.presentation.videos.VideoListViewModelFactory
import javax.inject.Inject


class VideoListActivity : AppCompatActivity() {
    @Inject
    lateinit var vmFactory: VideoListViewModelFactory
    private lateinit var vm: VideoListViewModel

    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_list)
        fragmentManager = supportFragmentManager
        supportActionBar?.hide()

        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.selectedItemId = R.id.video_bottom_menu

        (applicationContext as App).videoListAppComponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)[VideoListViewModel::class.java]

        val PERMISSION_REQUEST_CODE = 123
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }

        navigation.setOnNavigationItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.module_bottom_menu -> ModuleListFragment()
                R.id.video_bottom_menu -> VideoListFragment()
                else -> return@setOnNavigationItemSelectedListener false
            }

            replaceFragment(fragment)
            true
        }

        val initialFragment = VideoListFragment()
        replaceFragment(initialFragment)
    }

//    private fun replaceFragment(fragment: Fragment) {
//        val transaction = fragmentManager.beginTransaction()
//
//        transaction.replace(R.id.fragment_container, fragment)
//        transaction.commit()
//    }

    private fun replaceFragment(fragment: Fragment) {
        val fl = findViewById<FrameLayout>(R.id.fragment_container)
        fl.removeAllViews()
        val transaction1: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction1.add(R.id.fragment_container, fragment)
        transaction1.commit()
    }
}