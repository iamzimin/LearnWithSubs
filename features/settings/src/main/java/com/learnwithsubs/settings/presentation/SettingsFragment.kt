package com.learnwithsubs.settings.presentation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.settings.R
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

        val languages: Array<String> = arrayOf(getString(R.string.english), getString(R.string.russian), getString(R.string.spain))
        val styles: Array<String> = arrayOf(getString(R.string.light), getString(R.string.dark))
        val translatorSource: Array<String> = arrayOf(getString(R.string.server), getString(R.string.yandex), getString(R.string.android))
        val nativeLanguage: Array<String> = arrayOf(getString(R.string.english), getString(R.string.russian), getString(R.string.spain))
        val learningLanguage: Array<String> = arrayOf(getString(R.string.english), getString(R.string.russian), getString(R.string.spain))

        settingsFragmentBinding.languageText.text = vm.getAppLanguage()
        settingsFragmentBinding.styleText.text = vm.getAppStyle()
        settingsFragmentBinding.translatorSourceText.text = vm.getTranslatorSource()
        settingsFragmentBinding.nativeLanguageText.text = vm.getNativeLanguage()
        settingsFragmentBinding.learningLanguageText.text = vm.getLearningLanguage()


        val languageDialog = RadioButtonSelectionDialog(
            fragment = this@SettingsFragment,
            title = getString(R.string.language),
            options = languages,
            selected = settingsFragmentBinding.languageText.text.toString(),
        )
        val styleDialog = RadioButtonSelectionDialog(
            fragment = this@SettingsFragment,
            title = getString(R.string.style),
            options = styles,
            selected = settingsFragmentBinding.styleText.text.toString(),
        )
        val translatorSourceDialog = RadioButtonSelectionDialog(
            fragment = this@SettingsFragment,
            title = getString(R.string.translator_source),
            options = translatorSource,
            selected = settingsFragmentBinding.translatorSourceText.text.toString(),
        )
        val nativeLanguageDialog = RadioButtonSelectionDialog(
            fragment = this@SettingsFragment,
            title = getString(R.string.native_language),
            options = nativeLanguage,
            selected = settingsFragmentBinding.nativeLanguageText.text.toString(),
        )
        val learningLanguageDialog = RadioButtonSelectionDialog(
            fragment = this@SettingsFragment,
            title = getString(R.string.learning_language),
            options = learningLanguage,
            selected = settingsFragmentBinding.learningLanguageText.text.toString(),
        )


        settingsFragmentBinding.languageCard.setOnClickListener {
            languageDialog.openMenu()
        }
        languageDialog.setOnItemSelectedListener { selectedText ->
            vm.saveAppLanguage(language = selectedText)
            settingsFragmentBinding.languageText.text = selectedText
        }

        settingsFragmentBinding.styleCard.setOnClickListener {
            styleDialog.openMenu()
        }
        styleDialog.setOnItemSelectedListener { selectedText ->
            vm.saveAppStyle(appStyle = selectedText)
            settingsFragmentBinding.styleText.text = selectedText
        }

        settingsFragmentBinding.translatorSourceCard.setOnClickListener {
            translatorSourceDialog.openMenu()
        }
        translatorSourceDialog.setOnItemSelectedListener { selectedText ->
            vm.saveTranslatorSource(source = selectedText)
            settingsFragmentBinding.translatorSourceText.text = selectedText
            changeDownloadButtonVisibility(selectedText = selectedText)
        }

        settingsFragmentBinding.nativeLanguageCard.setOnClickListener {
            nativeLanguageDialog.openMenu()
        }
        nativeLanguageDialog.setOnItemSelectedListener { selectedText ->
            vm.saveNativeLanguage(nativeLanguage = selectedText)
            settingsFragmentBinding.nativeLanguageText.text = selectedText
        }

        settingsFragmentBinding.learningLanguageCard.setOnClickListener {
            learningLanguageDialog.openMenu()
        }
        learningLanguageDialog.setOnItemSelectedListener { selectedText ->
            vm.saveLearningLanguage(learningLanguage = selectedText)
            settingsFragmentBinding.learningLanguageText.text = selectedText
        }

        changeDownloadButtonVisibility(selectedText = settingsFragmentBinding.translatorSourceText.text.toString())

        return settingsFragmentBinding.root
    }

    private fun changeDownloadButtonVisibility(selectedText: String) {
        if (selectedText == getString(R.string.android)) {
            settingsFragmentBinding.nativeLanguageDownload.visibility = View.VISIBLE
            settingsFragmentBinding.learningLanguageDownload.visibility = View.VISIBLE
        } else {
            settingsFragmentBinding.nativeLanguageDownload.visibility = View.GONE
            settingsFragmentBinding.learningLanguageDownload.visibility = View.GONE
        }
    }
}