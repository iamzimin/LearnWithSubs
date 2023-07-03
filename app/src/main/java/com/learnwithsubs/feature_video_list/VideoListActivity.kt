package com.learnwithsubs.feature_video_list

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.learnwithsubs.R
import com.learnwithsubs.app.App
import com.learnwithsubs.feature_video_list.videos.VideoListViewModel
import com.learnwithsubs.feature_video_list.videos.VideoListViewModelFactory
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

        val navigation = findViewById<BottomNavigationView>(R.id.fragment_navigation)
        navigation.selectedItemId = R.id.video_bottom_menu

        (applicationContext as App).videoListAppComponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)[VideoListViewModel::class.java]

        val PERMISSION_REQUEST_CODE = 123
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }

        val navHostFragment: NavHostFragment? = fragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment?
        if (navHostFragment != null) { NavigationUI.setupWithNavController(
            findViewById<View>(R.id.fragment_navigation) as BottomNavigationView,
            navHostFragment.navController)
        }
    }
}