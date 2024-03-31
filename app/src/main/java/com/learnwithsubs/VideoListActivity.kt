package com.learnwithsubs

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.learnwithsubs.databinding.ActivityVideoListBinding
import com.learnwithsubs.video_list.presentation.VideoListFragment
import com.learnwithsubs.word_list.presentation.WordListFragment
import com.example.base.OnSelectionModeChange


class VideoListActivity : AppCompatActivity(), OnSelectionModeChange {
    companion object {
        const val PERMISSION_REQUEST_CODE = 123
    }
//    @Inject
//    lateinit var vmFactory: VideoListViewModelFactory
//    private lateinit var vm: VideoListViewModel

    private lateinit var videoListBinding: ActivityVideoListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoListBinding = ActivityVideoListBinding.inflate(layoutInflater)
        setContentView(videoListBinding.root)
        supportActionBar?.hide()

        videoListBinding.fragmentNavigation.selectedItemId = R.id.video_bottom_menu

//        (applicationContext as App).videoListAppComponent.inject(this)
//        vm = ViewModelProvider(this, vmFactory)[VideoListViewModel::class.java]

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }

        val navHostFragment: NavHostFragment? =
            supportFragmentManager.findFragmentById(videoListBinding.fragmentContainer.id) as NavHostFragment?
        if (navHostFragment != null) {
            NavigationUI.setupWithNavController(videoListBinding.fragmentNavigation, navHostFragment.navController)
        }

        navHostFragment?.childFragmentManager?.let { fragmentManager ->
            val fragment = fragmentManager.fragments.find { fragment -> fragment is VideoListFragment } as? VideoListFragment
            fragment?.attachSelectionMode(this@VideoListActivity)
        }

        navHostFragment?.childFragmentManager?.addFragmentOnAttachListener { _, fragment ->
            if (fragment is WordListFragment) {
                fragment.attachSelectionMode(this@VideoListActivity)
            }
        }
    }

    override fun selectionModeChange(isSelectionMode: Boolean) {
        videoListBinding.fragmentNavigation.visibility = if (!isSelectionMode) View.VISIBLE else View.GONE
    }
}