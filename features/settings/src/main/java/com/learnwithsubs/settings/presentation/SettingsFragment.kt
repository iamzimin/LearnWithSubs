package com.learnwithsubs.settings.presentation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.settings.databinding.FragmentSettingsBinding
import com.learnwithsubs.settings.di.DaggerSettingsAppComponent
import com.learnwithsubs.settings.di.SettingsAppModule
import com.learnwithsubs.settings.presentation.settings.SettingsViewModel
import com.learnwithsubs.settings.presentation.settings.SettingsViewModelFactory
import javax.inject.Inject


class SettingsFragment : Fragment() {
    @Inject
    lateinit var vmFactory: SettingsViewModelFactory
    private lateinit var vm: SettingsViewModel

    private lateinit var settingsFragmentBinding: FragmentSettingsBinding

    private val appContext: Context by lazy { requireContext() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        DaggerSettingsAppComponent.builder().settingsAppModule(SettingsAppModule(context = appContext)).build().inject(this)
        vm = ViewModelProvider(this, vmFactory)[SettingsViewModel::class.java]

        settingsFragmentBinding = FragmentSettingsBinding.inflate(inflater, container, false)


        settingsFragmentBinding.languageCard.setOnClickListener {

        }

        settingsFragmentBinding.styleCard.setOnClickListener {

        }

        settingsFragmentBinding.translatorSourceCard.setOnClickListener {

        }

        settingsFragmentBinding.nativeLanguageCard.setOnClickListener {

        }

        settingsFragmentBinding.learningLanguageCard.setOnClickListener {

        }

        return settingsFragmentBinding.root
    }

}