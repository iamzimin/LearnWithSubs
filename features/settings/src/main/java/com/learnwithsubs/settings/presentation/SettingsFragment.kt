package com.learnwithsubs.settings.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.settings.databinding.FragmentSettingsBinding
import com.google.android.gms.tasks.Task
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.learnwithsubs.resource.R
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
    private val remoteModel: RemoteModelManager = RemoteModelManager.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        DaggerSettingsAppComponent.builder().settingsAppModule(SettingsAppModule(context = appContext)).build().inject(this)
        vm = ViewModelProvider(this, vmFactory)[SettingsViewModel::class.java]

        settingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)

        vm.updateLanguagePair()

        val languages: Array<String> = vm.getAllAppLanguages()
        val styles: Array<String> = vm.getAllStyles()
        val translatorSource: Array<String> = vm.getAllTranslatorSource()
        val translatorLanguages: Array<String> = vm.getAllTranslatorLanguages()

        settingsBinding.languageText.text = vm.getAppLanguage().first
        settingsBinding.styleText.text = vm.getAppStyle()
        settingsBinding.translatorSourceText.text = vm.getTranslatorSource()
        settingsBinding.nativeLanguageText.text = vm.nativeLanguagePair.first
        settingsBinding.learningLanguageText.text = vm.learningLanguagePair.first


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
            options = translatorLanguages,
            selected = vm.nativeLanguagePair.first,
        )
        val learningLanguageDialog = RadioButtonSelectionDialog(
            fragment = this@SettingsFragment,
            title = getString(R.string.learning_language),
            options = translatorLanguages,
            selected = vm.learningLanguagePair.first,
        )


        // Language change
        settingsBinding.languageCard.setOnClickListener {
            languageDialog.openMenu()
        }
        languageDialog.setOnItemSelectedListener { selectedText ->
            vm.saveAppLanguage(language = selectedText)
            settingsBinding.languageText.text = selectedText
            activity?.recreate()
        }

        // Style change
        settingsBinding.styleCard.setOnClickListener {
            styleDialog.openMenu()
        }
        styleDialog.setOnItemSelectedListener { selectedText ->
            vm.saveAppStyle(appStyle = selectedText)
            settingsBinding.styleText.text = selectedText
            activity?.recreate()
        }

        // Translator type change
        settingsBinding.translatorSourceCard.setOnClickListener {
            translatorSourceDialog.openMenu()
        }
        settingsBinding.translatorSourceCard.isClickable = false
        translatorSourceDialog.setOnItemSelectedListener { selectedText ->
            vm.saveTranslatorSource(source = selectedText)
            settingsBinding.translatorSourceText.text = selectedText
            changeLanguageCardVisibility(selectedText = selectedText)
        }

        // Native language change
        settingsBinding.nativeLanguageCard.setOnClickListener {
            nativeLanguageDialog.openMenu()
        }
        settingsBinding.nativeLanguageCard.isClickable = false
        nativeLanguageDialog.setOnItemSelectedListener { selectedText ->
            vm.saveNativeLanguage(nativeLanguage = selectedText)
            vm.nativeLanguagePair = vm.getNativeLanguage()
            settingsBinding.nativeLanguageText.text = vm.nativeLanguagePair.first
            updateNativeCard()
        }

        // Learning language change
        settingsBinding.learningLanguageCard.setOnClickListener {
            learningLanguageDialog.openMenu()
        }
        settingsBinding.learningLanguageCard.isClickable = false
        learningLanguageDialog.setOnItemSelectedListener { selectedText ->
            vm.saveLearningLanguage(learningLanguage = selectedText)
            vm.learningLanguagePair = vm.getLearningLanguage()
            settingsBinding.learningLanguageText.text = vm.learningLanguagePair.first
            updateLearningCard()
        }


        settingsBinding.nativeLanguageDownload.setOnClickListener {
            val model = TranslateRemoteModel.Builder(vm.nativeLanguagePair.second).build()
            vm.nativeDownloadingTask = remoteModel.download(model, DownloadConditions.Builder().build())
            attachNativeTaskCallback(task = vm.nativeDownloadingTask)
            changeSourceCardAvailability(false)
            changeNativeCardAvailability(false)
            showOnlyInGroup(settingsBinding.nativeLanguageProgress, settingsBinding.nativeGroup)
        }

        settingsBinding.learningLanguageDownload.setOnClickListener {
            val model = TranslateRemoteModel.Builder(vm.learningLanguagePair.second).build()
            vm.learningDownloadingTask = remoteModel.download(model, DownloadConditions.Builder().build())
            attachLearningTaskCallback(task = vm.learningDownloadingTask)
            changeSourceCardAvailability(false)
            changeLearningCardAvailability(false)
            showOnlyInGroup(settingsBinding.learningLanguageProgress, settingsBinding.learningGroup)
        }

        changeLanguageCardVisibility(selectedText = settingsBinding.translatorSourceText.text.toString())
        attachNativeTaskCallback(task = vm.nativeDownloadingTask)
        attachLearningTaskCallback(task = vm.learningDownloadingTask)
        changeSourceCardAvailability(isAvailable = (vm.learningDownloadingTask?.isSuccessful ?: true) && (vm.nativeDownloadingTask?.isSuccessful ?: true))

        return settingsBinding.root
    }

    private fun updateLanguagesCard() {
        changeSourceCardAvailability(isAvailable = (vm.learningDownloadingTask?.isSuccessful ?: true) && (vm.nativeDownloadingTask?.isSuccessful ?: true))
        updateNativeCard()
        updateLearningCard()
    }
    private fun updateLearningCard() {
        remoteModel.getDownloadedModels(TranslateRemoteModel::class.java)
            .addOnSuccessListener { models ->
                val isLearningModelDownloaded = models.any { it.language == vm.learningLanguagePair.second }
                val isLearningModelDownloadingComplete = vm.learningDownloadingTask?.isSuccessful ?: true

                changeLearningCardAvailability(isLearningModelDownloadingComplete)
                if (isLearningModelDownloaded) {
                    showOnlyInGroup(settingsBinding.learningLanguageCheck, settingsBinding.learningGroup)
                } else if (!isLearningModelDownloadingComplete) {
                    showOnlyInGroup(settingsBinding.learningLanguageProgress, settingsBinding.learningGroup)
                } else {
                    showOnlyInGroup(settingsBinding.learningLanguageDownload, settingsBinding.learningGroup)
                }
            }
    }
    private fun updateNativeCard() {
        remoteModel.getDownloadedModels(TranslateRemoteModel::class.java)
            .addOnSuccessListener { models ->
                val isNativeModelDownloaded = models.any { it.language == vm.nativeLanguagePair.second }
                val isNativeModelDownloadingComplete = vm.nativeDownloadingTask?.isSuccessful ?: true

                changeNativeCardAvailability(isNativeModelDownloadingComplete)
                if (isNativeModelDownloaded) {
                    showOnlyInGroup(settingsBinding.nativeLanguageCheck, settingsBinding.nativeGroup)
                } else if (!isNativeModelDownloadingComplete) {
                    showOnlyInGroup(settingsBinding.nativeLanguageProgress, settingsBinding.nativeGroup)
                } else {
                    showOnlyInGroup(settingsBinding.nativeLanguageDownload, settingsBinding.nativeGroup)
                }
            }
    }


    private fun isAndroidSourceSelected(): Boolean {
        return try {
            return settingsBinding.translatorSourceText.text.toString() == getString(R.string.android)
        } catch (_: Exception) { false }
    }

    private fun showOnlyInGroup(viewToShow: View?, group: LinearLayout) {
        var newViewToShow: View? = viewToShow
        if (!isAndroidSourceSelected()) {
            newViewToShow = null
        }

        for (layout in group) {
            if (layout == newViewToShow) {
                layout.visibility = View.VISIBLE
            } else {
                layout.visibility = View.GONE
            }
        }
    }

    private fun changeLanguageCardVisibility(selectedText: String) {
        try {
            if (selectedText == getString(R.string.android)) {
                updateLanguagesCard()
            } else {
                changeNativeCardAvailability(true)
                changeLearningCardAvailability(true)
                showOnlyInGroup(null, settingsBinding.learningGroup)
                showOnlyInGroup(null, settingsBinding.nativeGroup)
            }
        } catch (_: Exception) { }
    }

    private fun changeNativeCardAvailability(isAvailable: Boolean) {
        settingsBinding.nativeLanguageCard.isClickable = isAvailable
        if (isAvailable) {
            settingsBinding.nativeLanguageTitle.setTextAppearance(R.style.RadioButtonTitleTextAvailable)
            settingsBinding.nativeLanguageText.setTextAppearance(R.style.RadioButtonSubTitleTextAvailable)
        } else {
            settingsBinding.nativeLanguageTitle.setTextAppearance(R.style.RadioButtonTitleTextUnavailable)
            settingsBinding.nativeLanguageText.setTextAppearance(R.style.RadioButtonSubTitleTextUnavailable)
        }
    }
    private fun changeLearningCardAvailability(isAvailable: Boolean) {
        settingsBinding.learningLanguageCard.isClickable = isAvailable
        if (isAvailable) {
            settingsBinding.learningLanguageTitle.setTextAppearance(R.style.RadioButtonTitleTextAvailable)
            settingsBinding.learningLanguageText.setTextAppearance(R.style.RadioButtonSubTitleTextAvailable)
        } else {
            settingsBinding.learningLanguageTitle.setTextAppearance(R.style.RadioButtonTitleTextUnavailable)
            settingsBinding.learningLanguageText.setTextAppearance(R.style.RadioButtonSubTitleTextUnavailable)
        }
    }
    private fun changeSourceCardAvailability(isAvailable: Boolean) {
        settingsBinding.translatorSourceCard.isClickable = isAvailable
        if (isAvailable) {
            settingsBinding.translatorSourceTitle.setTextAppearance(R.style.RadioButtonTitleTextAvailable)
            settingsBinding.translatorSourceText.setTextAppearance(R.style.RadioButtonSubTitleTextAvailable)
        } else {
            settingsBinding.translatorSourceTitle.setTextAppearance(R.style.RadioButtonTitleTextUnavailable)
            settingsBinding.translatorSourceText.setTextAppearance(R.style.RadioButtonSubTitleTextUnavailable)
        }
    }

    private fun attachLearningTaskCallback(task: Task<Void>?) {
        task?.addOnSuccessListener {
                changeSourceCardAvailability(isAvailable = (vm.learningDownloadingTask?.isSuccessful ?: true) && (vm.nativeDownloadingTask?.isSuccessful ?: true))
                changeLearningCardAvailability(true)
                showOnlyInGroup(settingsBinding.learningLanguageCheck, settingsBinding.learningGroup)
            }?.addOnFailureListener {
                changeSourceCardAvailability(isAvailable = (vm.learningDownloadingTask?.isSuccessful ?: true) && (vm.nativeDownloadingTask?.isSuccessful ?: true))
                changeLearningCardAvailability(true)
                showOnlyInGroup(settingsBinding.learningLanguageDownload, settingsBinding.learningGroup)
            }?.addOnCanceledListener {
                changeSourceCardAvailability(isAvailable = (vm.learningDownloadingTask?.isSuccessful ?: true) && (vm.nativeDownloadingTask?.isSuccessful ?: true))
                changeLearningCardAvailability(true)
                showOnlyInGroup(null, settingsBinding.learningGroup)
            }
    }
    private fun attachNativeTaskCallback(task: Task<Void>?) {
        task?.addOnSuccessListener {
                changeSourceCardAvailability(isAvailable = (vm.learningDownloadingTask?.isSuccessful ?: true) && (vm.nativeDownloadingTask?.isSuccessful ?: true))
                changeNativeCardAvailability(true)
                showOnlyInGroup(settingsBinding.nativeLanguageCheck, settingsBinding.nativeGroup)
            }?.addOnFailureListener {
                changeSourceCardAvailability(isAvailable = (vm.learningDownloadingTask?.isSuccessful ?: true) && (vm.nativeDownloadingTask?.isSuccessful ?: true))
                changeNativeCardAvailability(true)
                showOnlyInGroup(settingsBinding.nativeLanguageDownload, settingsBinding.nativeGroup)
            }?.addOnCanceledListener {
                changeSourceCardAvailability(isAvailable = (vm.learningDownloadingTask?.isSuccessful ?: true) && (vm.nativeDownloadingTask?.isSuccessful ?: true))
                changeNativeCardAvailability(true)
                showOnlyInGroup(null, settingsBinding.nativeGroup)
            }
    }


    override fun onResume() {
        super.onResume()
        if (isAndroidSourceSelected()) {
            updateLanguagesCard()
        }
    }
}