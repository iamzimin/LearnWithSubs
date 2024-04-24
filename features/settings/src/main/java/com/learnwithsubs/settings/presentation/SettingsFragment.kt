package com.learnwithsubs.settings.presentation

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.settings.R
import com.example.settings.databinding.FragmentSettingsBinding
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.learnwithsubs.settings.di.DaggerSettingsAppComponent
import com.learnwithsubs.settings.di.SettingsAppModule
import com.learnwithsubs.settings.presentation.settings.SettingsViewModel
import com.learnwithsubs.settings.presentation.settings.SettingsViewModelFactory
import javax.inject.Inject


class SettingsFragment : Fragment() {
    @Inject
    lateinit var vmFactory: SettingsViewModelFactory
    private lateinit var vm: SettingsViewModel

    private lateinit var settingsBinding: FragmentSettingsBinding

    private val appContext: Context by lazy { requireContext() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        DaggerSettingsAppComponent.builder().settingsAppModule(SettingsAppModule(context = appContext)).build().inject(this)
        vm = ViewModelProvider(this, vmFactory)[SettingsViewModel::class.java]

        settingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)

        val languages: Array<String> = arrayOf(getString(R.string.english), getString(R.string.russian), getString(R.string.spain))
        val styles: Array<String> = arrayOf(getString(R.string.light), getString(R.string.dark))
        val translatorSource: Array<String> = arrayOf(getString(R.string.server), getString(R.string.yandex), getString(R.string.android))
        val nativeLanguage: Array<String> = arrayOf(getString(R.string.english), getString(R.string.russian), getString(R.string.spain), getString(R.string.french), getString(R.string.japanese), getString(R.string.italian), getString(R.string.german))
        val learningLanguage: Array<String> =  arrayOf(getString(R.string.english), getString(R.string.russian), getString(R.string.spain), getString(R.string.french), getString(R.string.japanese), getString(R.string.italian), getString(R.string.german))

        settingsBinding.languageText.text = vm.getAppLanguage()
        settingsBinding.styleText.text = vm.getAppStyle()
        settingsBinding.translatorSourceText.text = vm.getTranslatorSource()
        settingsBinding.nativeLanguageText.text = vm.getNativeLanguage()
        settingsBinding.learningLanguageText.text = vm.getLearningLanguage()


        val languageDialog = RadioButtonSelectionDialog(
            fragment = this@SettingsFragment,
            title = getString(R.string.language),
            options = languages,
            selected = settingsBinding.languageText.text.toString(),
        )
        val styleDialog = RadioButtonSelectionDialog(
            fragment = this@SettingsFragment,
            title = getString(R.string.style),
            options = styles,
            selected = settingsBinding.styleText.text.toString(),
        )
        val translatorSourceDialog = RadioButtonSelectionDialog(
            fragment = this@SettingsFragment,
            title = getString(R.string.translator_source),
            options = translatorSource,
            selected = settingsBinding.translatorSourceText.text.toString(),
        )
        val nativeLanguageDialog = RadioButtonSelectionDialog(
            fragment = this@SettingsFragment,
            title = getString(R.string.native_language),
            options = nativeLanguage,
            selected = settingsBinding.nativeLanguageText.text.toString(),
        )
        val learningLanguageDialog = RadioButtonSelectionDialog(
            fragment = this@SettingsFragment,
            title = getString(R.string.learning_language),
            options = learningLanguage,
            selected = settingsBinding.learningLanguageText.text.toString(),
        )


        // Language change
        settingsBinding.languageCard.setOnClickListener {
            languageDialog.openMenu()
        }
        languageDialog.setOnItemSelectedListener { selectedText ->
            vm.saveAppLanguage(language = selectedText)
            settingsBinding.languageText.text = selectedText
        }

        // Style change
        settingsBinding.styleCard.setOnClickListener {
            styleDialog.openMenu()
        }
        styleDialog.setOnItemSelectedListener { selectedText ->
            vm.saveAppStyle(appStyle = selectedText)
            settingsBinding.styleText.text = selectedText
        }

        // Translator type change
        settingsBinding.translatorSourceCard.setOnClickListener {
            translatorSourceDialog.openMenu()
        }
        translatorSourceDialog.setOnItemSelectedListener { selectedText ->
            vm.saveTranslatorSource(source = selectedText)
            settingsBinding.translatorSourceText.text = selectedText
            changeDownloadButtonVisibility(selectedText = selectedText)
        }

        // Native language change
        settingsBinding.nativeLanguageCard.setOnClickListener {
            nativeLanguageDialog.openMenu()
        }
        nativeLanguageDialog.setOnItemSelectedListener { selectedText ->
            vm.saveNativeLanguage(nativeLanguage = selectedText)
            settingsBinding.nativeLanguageText.text = selectedText
            checkIsLanguagesDownload()
        }

        // Learning language change
        settingsBinding.learningLanguageCard.setOnClickListener {
            learningLanguageDialog.openMenu()
        }
        learningLanguageDialog.setOnItemSelectedListener { selectedText ->
            vm.saveLearningLanguage(learningLanguage = selectedText)
            settingsBinding.learningLanguageText.text = selectedText
            checkIsLanguagesDownload()
        }


        settingsBinding.nativeLanguageDownload.setOnClickListener{
            val modelManager = RemoteModelManager.getInstance()
            val language = vm.getTwoFirst(settingsBinding.nativeLanguageText.text.toString())
            val model = TranslateRemoteModel.Builder(language).build()
            modelManager.download(model, DownloadConditions.Builder().build())
                .addOnSuccessListener {
                    showOnlyNative(settingsBinding.nativeLanguageCheck)
                }
                .addOnFailureListener {
                    showOnlyNative(settingsBinding.nativeLanguageDownload)
                }
            showOnlyNative(settingsBinding.nativeLanguageProgress)
        }

        settingsBinding.learningLanguageDownload.setOnClickListener{
            val modelManager = RemoteModelManager.getInstance()
            val language = vm.getTwoFirst(settingsBinding.learningLanguageText.text.toString())
            val model = TranslateRemoteModel.Builder(language).build()
            modelManager.download(model, DownloadConditions.Builder().build())
                .addOnSuccessListener {
                    showOnlyLearning(settingsBinding.learningLanguageCheck)
                }
                .addOnFailureListener {
                    showOnlyLearning(settingsBinding.learningLanguageDownload)
                }
            showOnlyLearning(settingsBinding.learningLanguageProgress)
        }

        changeDownloadButtonVisibility(selectedText = settingsBinding.translatorSourceText.text.toString())
        checkIsLanguagesDownload()


        return settingsBinding.root
    }

    private fun checkIsLanguagesDownload() {
        RemoteModelManager.getInstance().getDownloadedModels(TranslateRemoteModel::class.java)
            .addOnSuccessListener { models ->
                val isNativeModelDownloaded = models.any { it.language == vm.getTwoFirst(settingsBinding.nativeLanguageText.text.toString()) }
                val isLearningModelDownloaded = models.any { it.language == vm.getTwoFirst(settingsBinding.learningLanguageText.text.toString()) }

                if (isNativeModelDownloaded) {
                    showOnlyNative(settingsBinding.nativeLanguageCheck)
                } else {
                    showOnlyNative(settingsBinding.nativeLanguageDownload)
                }

                if (isLearningModelDownloaded) {
                    showOnlyLearning(settingsBinding.learningLanguageCheck)
                } else {
                    showOnlyLearning(settingsBinding.learningLanguageDownload)
                }
            }
    }

    private fun showOnlyLearning(viewToShow: View?) {
        for (layout in settingsBinding.learningGroup) {
            if (layout == viewToShow) {
                layout.visibility = View.VISIBLE
            } else {
                layout.visibility = View.GONE
            }
        }
    }
    private fun showOnlyNative(viewToShow: View?) {
        for (layout in settingsBinding.nativeGroup) {
            if (layout == viewToShow) {
                layout.visibility = View.VISIBLE
            } else {
                layout.visibility = View.GONE
            }
        }
    }

    

    private fun changeDownloadButtonVisibility(selectedText: String) {
        if (selectedText == getString(R.string.android)) {
            checkIsLanguagesDownload()
        } else {
            showOnlyLearning(null)
            showOnlyNative(null)
        }
    }
}