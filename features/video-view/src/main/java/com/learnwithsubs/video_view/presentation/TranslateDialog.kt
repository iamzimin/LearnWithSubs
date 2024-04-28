package com.learnwithsubs.video_view.presentation

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.speech.tts.TextToSpeech
import android.view.Window
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnwithsubs.video_view.R
import com.learnwithsubs.video_view.databinding.DialogTranslateBinding
import com.learnwithsubs.video_view.domain.models.TranslationModel
import com.learnwithsubs.video_view.domain.models.WordTranslation
import com.learnwithsubs.video_view.presentation.adapter.DictionaryAdapter
import com.learnwithsubs.video_view.presentation.adapter.OnDictionaryClick
import com.learnwithsubs.video_view.presentation.videos.VideoViewViewModel
import java.util.Date
import java.util.Locale

class TranslateDialog(activity: Activity, private val vm: VideoViewViewModel) : OnDictionaryClick, TextToSpeech.OnInitListener {
    private val context = activity.applicationContext
    private val translateDialogBind: DialogTranslateBinding = DialogTranslateBinding.inflate(activity.layoutInflater)
    private val dialogMenu = Dialog(activity)
    private val adapter = DictionaryAdapter(wordsInit = ArrayList())

    private var ttsFrom: TextToSpeech
    private var ttsTo: TextToSpeech

    private val nativeLanguage: Pair<String, String> = vm.getNativeLanguage()
    private val learnLanguage: Pair<String, String> = vm.getLearningLanguage()
    private val translatorSource: String = vm.getTranslatorSource()

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            ttsFrom.language = Locale(nativeLanguage.second)
            ttsTo.language = Locale(learnLanguage.second)
        }
    }

    fun openTranslateDialog() {
        val translationModel = TranslationModel(
            word = vm.textToTranslate,
            inputLanguage = learnLanguage.second,
            outputLanguage = nativeLanguage.second,
        )
        when (translatorSource) {
            context.getString(com.learnwithsubs.shared_preference_settings.R.string.server) ->
                vm.getTranslationFromServer(translationModel = translationModel)
            context.getString(com.learnwithsubs.shared_preference_settings.R.string.yandex) ->
                vm.getTranslationFromYandexDictionary(translationModel = translationModel)
            context.getString(com.learnwithsubs.shared_preference_settings.R.string.android) ->
                vm.getTranslationFromAndroid(translationModel = translationModel)
            else -> { }
        }

        translateDialogBind.inputWord.setText(translationModel.word)
        translateDialogBind.inputWord.clearFocus()

        dialogMenu.show()
    }

    init {
        dialogMenu.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogMenu.setContentView(R.layout.dialog_translate)

        adapter.setOnItemClickListener(this@TranslateDialog)

        dialogMenu.setContentView(translateDialogBind.root)
        translateDialogBind.dictionaryRecycler.layoutManager = LinearLayoutManager(context)
        translateDialogBind.dictionaryRecycler.adapter = adapter
        translateDialogBind.translatorType.text = translatorSource

        translateDialogBind.inputLanguage.text = learnLanguage.first
        translateDialogBind.outputLanguage.text = nativeLanguage.first

        val itemDecoration = DictionaryAdapter.RecyclerViewItemDecoration(10)
        translateDialogBind.dictionaryRecycler.addItemDecoration(itemDecoration)

        if (dialogMenu.window != null) {
            dialogMenu.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }


        dialogMenu.setOnDismissListener {
            translateDialogBind.inputWord.setText("")
            translateDialogBind.outputWord.setText("")
            adapter.updateData(wordsList = ArrayList())
        }


        ttsFrom = TextToSpeech(context, this)
        ttsTo = TextToSpeech(context, this)

        translateDialogBind.audioInputWord.setOnClickListener {
            ttsFrom.speak(translateDialogBind.inputWord.text, TextToSpeech.QUEUE_FLUSH, null, "")
        }
        translateDialogBind.audioOutputWord.setOnClickListener {
            ttsTo.speak(translateDialogBind.outputWord.text, TextToSpeech.QUEUE_FLUSH, null, "")
        }

        translateDialogBind.saveWord.setOnClickListener {
            vm.saveWord(
                WordTranslation(
                    word = translateDialogBind.inputWord.text.toString(),
                    translation = translateDialogBind.outputWord.text.toString(),
                    nativeLanguage = nativeLanguage.second, learnLanguage = learnLanguage.second,
                    videoID = vm.currentVideo?.id, videoName = vm.currentVideo?.name,
                    timestamp = Date().time,
                )
            )
        }


        // Translate
        vm.dictionaryWordsLiveData.observe(activity as LifecycleOwner) { dict ->
            translateDialogBind.outputWord.setText(dict.translation)
            translateDialogBind.outputWord.clearFocus()
            adapter.updateData(wordsList = dict.dictionaryElement)
        }
        vm.translatorTranslationLiveData.observe(activity as LifecycleOwner) { transl ->
            transl ?: Toast.makeText(context, R.string.server_for_translation_is_not_available, Toast.LENGTH_SHORT).show()
            val text = transl ?: return@observe
            translateDialogBind.outputWord.setText(text)
            translateDialogBind.outputWord.clearFocus()
        }
    }




    override fun onItemClick(similarWord: String, similarWordTranslate: String) {
        translateDialogBind.inputWord.setText(similarWord)
        translateDialogBind.outputWord.setText(similarWordTranslate)
    }
}