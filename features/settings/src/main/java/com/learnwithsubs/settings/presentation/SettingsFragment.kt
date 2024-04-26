package com.learnwithsubs.settings.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import java.lang.reflect.Field
import javax.inject.Inject


class SettingsFragment : Fragment() {
    @Inject
    lateinit var vmFactory: SettingsViewModelFactory
    private lateinit var vm: SettingsViewModel

    private lateinit var settingsBinding: FragmentSettingsBinding

    private lateinit var nativeLanguagePair: Pair<String, String>
    private lateinit var learningLanguagePair: Pair<String, String>
    private val appContext: Context by lazy { requireContext() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        DaggerSettingsAppComponent.builder().settingsAppModule(SettingsAppModule(context = appContext)).build().inject(this)
        vm = ViewModelProvider(this, vmFactory)[SettingsViewModel::class.java]

        settingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)

        val languages: Array<String> = vm.getAllAppLanguages()
        val styles: Array<String> = vm.getAllStyles()
        val translatorSource: Array<String> = vm.getAllTranslatorSource()
        val nativeLanguage: Array<String> = vm.getAllTranslatorLanguages()
        val learningLanguage: Array<String> = vm.getAllTranslatorLanguages()

        nativeLanguagePair = vm.getNativeLanguage()
        learningLanguagePair = vm.getLearningLanguage()

        settingsBinding.languageText.text = vm.getAppLanguage().first
        settingsBinding.styleText.text = vm.getAppStyle()
        settingsBinding.translatorSourceText.text = vm.getTranslatorSource()
        settingsBinding.nativeLanguageText.text = nativeLanguagePair.first
        settingsBinding.learningLanguageText.text = learningLanguagePair.first


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
            selected = nativeLanguagePair.first,
        )
        val learningLanguageDialog = RadioButtonSelectionDialog(
            fragment = this@SettingsFragment,
            title = getString(R.string.learning_language),
            options = learningLanguage,
            selected = learningLanguagePair.first,
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
            nativeLanguagePair = vm.getNativeLanguage()
            settingsBinding.nativeLanguageText.text = nativeLanguagePair.first
            checkIsNativeDownloaded()
        }

        // Learning language change
        settingsBinding.learningLanguageCard.setOnClickListener {
            learningLanguageDialog.openMenu()
        }
        learningLanguageDialog.setOnItemSelectedListener { selectedText ->
            vm.saveLearningLanguage(learningLanguage = selectedText)
            learningLanguagePair = vm.getLearningLanguage()
            settingsBinding.learningLanguageText.text = learningLanguagePair.first
            checkIsLearningDownloaded()
        }


        settingsBinding.nativeLanguageDownload.setOnClickListener{
            val modelManager = RemoteModelManager.getInstance()
            val model = TranslateRemoteModel.Builder(nativeLanguagePair.second).build()
            modelManager.download(model, DownloadConditions.Builder().build())
                .addOnSuccessListener {
                    showOnlyNative(settingsBinding.nativeLanguageCheck)
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    showOnlyNative(settingsBinding.nativeLanguageDownload)
                }
                .addOnCanceledListener {
                    showOnlyNative(null)
                }
            showOnlyNative(settingsBinding.nativeLanguageProgress)
        }

        settingsBinding.learningLanguageDownload.setOnClickListener{
            val modelManager = RemoteModelManager.getInstance()
            val model = TranslateRemoteModel.Builder(learningLanguagePair.second).build()
            modelManager.download(model, DownloadConditions.Builder().build())
                .addOnSuccessListener {
                    showOnlyLearning(settingsBinding.learningLanguageCheck)
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    showOnlyLearning(settingsBinding.learningLanguageDownload)
                }
                .addOnCanceledListener {
                    showOnlyNative(null)
                }
            showOnlyLearning(settingsBinding.learningLanguageProgress)
        }

        changeDownloadButtonVisibility(selectedText = settingsBinding.translatorSourceText.text.toString())

        return settingsBinding.root
    }

    private fun checkIsLanguagesDownload() {
        checkIsNativeDownloaded()
        checkIsLearningDownloaded()
    }
    private fun checkIsLearningDownloaded() {
        RemoteModelManager.getInstance().getDownloadedModels(TranslateRemoteModel::class.java)
            .addOnSuccessListener { models ->
                val isLearningModelDownloaded = models.any { it.language == learningLanguagePair.second }
                if (isLearningModelDownloaded) {
                    showOnlyLearning(settingsBinding.learningLanguageCheck)
                } else {
                    showOnlyLearning(settingsBinding.learningLanguageDownload)
                }
            }
    }
    private fun checkIsNativeDownloaded() {
        RemoteModelManager.getInstance().getDownloadedModels(TranslateRemoteModel::class.java)
            .addOnSuccessListener { models ->
                val isNativeModelDownloaded = models.any { it.language == nativeLanguagePair.second }
                if (isNativeModelDownloaded) {
                    showOnlyNative(settingsBinding.nativeLanguageCheck)
                } else {
                    showOnlyNative(settingsBinding.nativeLanguageDownload)
                }
            }
    }

    private fun showOnlyLearning(viewToShow: View?) {
        var newViewToShow: View? = viewToShow
        if (settingsBinding.translatorSourceText.text.toString() != getString(com.learnwithsubs.shared_preference_settings.R.string.android)) {
            newViewToShow = null
        }

        for (layout in settingsBinding.learningGroup) {
            if (layout == newViewToShow) {
                layout.visibility = View.VISIBLE
            } else {
                layout.visibility = View.GONE
            }
        }
    }
    private fun showOnlyNative(viewToShow: View?) {
        var newViewToShow: View? = viewToShow
        if (settingsBinding.translatorSourceText.text.toString() != getString(com.learnwithsubs.shared_preference_settings.R.string.android)) {
            newViewToShow = null
        }

        for (layout in settingsBinding.nativeGroup) {
            if (layout == newViewToShow) {
                layout.visibility = View.VISIBLE
            } else {
                layout.visibility = View.GONE
            }
        }
    }

    private fun changeDownloadButtonVisibility(selectedText: String) {
        if (selectedText == getString(com.learnwithsubs.shared_preference_settings.R.string.android)) {
            checkIsLanguagesDownload()
        } else {
            showOnlyLearning(null)
            showOnlyNative(null)
        }
        val modelManager = RemoteModelManager.getInstance()
    }
}