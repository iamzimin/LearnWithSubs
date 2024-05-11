package com.learnwithsubs

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.base.OnSelectionModeChange
import com.learnwithsubs.databinding.ActivityVideoListBinding
import com.learnwithsubs.shared_preference_settings.data.repository.SharedPreferenceSettingsImpl
import com.learnwithsubs.video_list.presentation.VideoListFragment
import com.learnwithsubs.word_list.presentation.WordListFragment


class VideoListActivity : AppCompatActivity(), OnSelectionModeChange {
    companion object {
        const val PERMISSION_REQUEST_CODE = 123
    }
    private lateinit var videoListBinding: ActivityVideoListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val spsLanguage = SharedPreferenceSettingsImpl(context = baseContext)
        spsLanguage.saveAppLanguage(spsLanguage.getAppLanguage().first)
        val spsStyle = SharedPreferenceSettingsImpl(context = this)
        spsStyle.saveAppStyle(spsStyle.getAppStyle())

        super.onCreate(savedInstanceState)


        videoListBinding = ActivityVideoListBinding.inflate(layoutInflater)
        setContentView(videoListBinding.root)
        supportActionBar?.hide()

        videoListBinding.fragmentNavigation.selectedItemId = R.id.video_bottom_menu

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }

        val navHostFragment: NavHostFragment? = videoListBinding.fragmentContainer.getFragment()
        if (navHostFragment != null) {
            NavigationUI.setupWithNavController(videoListBinding.fragmentNavigation, navHostFragment.navController)
        }


        navHostFragment?.childFragmentManager?.let { fragmentManager ->
            val fragment = fragmentManager.fragments.find { fragment -> fragment is VideoListFragment } as? VideoListFragment
            fragment?.attachSelectionMode(this@VideoListActivity)
        }

        navHostFragment?.childFragmentManager?.addOnBackStackChangedListener {
            for (fragment in navHostFragment.childFragmentManager.fragments) {
                if (fragment is WordListFragment) {
                    fragment.attachSelectionMode(this@VideoListActivity)
                } else if (fragment is VideoListFragment) {
                    fragment.attachSelectionMode(this@VideoListActivity)
                }
            }
        }
    }

    override fun selectionModeChange(isSelectionMode: Boolean) {
        videoListBinding.fragmentNavigation.visibility = if (!isSelectionMode) View.VISIBLE else View.GONE
    }
}