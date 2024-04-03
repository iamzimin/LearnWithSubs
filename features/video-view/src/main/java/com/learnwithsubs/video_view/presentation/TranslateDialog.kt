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
import com.google.mlkit.nl.translate.TranslateLanguage
import com.learnwithsubs.video_view.R
import com.learnwithsubs.video_view.databinding.DialogTranslateBinding
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

    private lateinit var ttsFrom: TextToSpeech
    private lateinit var ttsTo: TextToSpeech

    private var nativeLanguage = TranslateLanguage.RUSSIAN
    private var learnLanguage = TranslateLanguage.ENGLISH


    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            ttsFrom.language = Locale(nativeLanguage)
            ttsTo.language = Locale(learnLanguage)
        }
    }

    fun openTranslateDialog() {
        val textToTranslate = vm.textToTranslate

        vm.getWordsFromDictionary(
            inputLang = learnLanguage,
            outputLang = nativeLanguage,
            word = textToTranslate
        )

        translateDialogBind.inputWord.setText(textToTranslate)
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
        translateDialogBind.translatorType.text = "Yandex Translator" // TODO select the type of translator from the settings

        val itemDecoration = DictionaryAdapter.RecyclerViewItemDecoration(10)
        translateDialogBind.dictionaryRecycler.addItemDecoration(itemDecoration)

        if (dialogMenu.window != null) {
            dialogMenu.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        getLanguageFromSettings()



        dialogMenu.setOnDismissListener {
            translateDialogBind.inputWord.setText("")
            translateDialogBind.outputWord.setText("")
            adapter.updateData(wordsList = ArrayList())
        }

        translateDialogBind.audioInputWord.setOnClickListener {
            ttsFrom.speak(translateDialogBind.inputWord.text, TextToSpeech.QUEUE_FLUSH, null, "")
        }
        translateDialogBind.audioOutputWord.setOnClickListener {
            ttsTo.speak(translateDialogBind.outputWord.text, TextToSpeech.QUEUE_FLUSH, null, "")
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
            vm.saveWord( //TODO
                WordTranslation(
                    word = translateDialogBind.inputWord.text.toString(),
                    translation = translateDialogBind.outputWord.text.toString(),
                    nativeLanguage = "ru", learnLanguage = "en",
                    videoID = vm.currentVideo?.id, videoName = vm.currentVideo?.name,
                    timestamp = Date().time,
                )
            )
        }


        // Translate
        vm.dictionaryWordsLiveData.observe(activity as LifecycleOwner) { dict ->
            if (dict == null) { //TODO вынести словарь и перевод в одну фнккцию
                vm.getFullTranslation(word = vm.textToTranslate, inputLang = learnLanguage, outputLang = nativeLanguage)
            } else {
                translateDialogBind.outputWord.setText(dict.translation)
                translateDialogBind.outputWord.clearFocus()
                val updatedPartSpeech = vm.changePartSpeech(context = context.applicationContext, list = dict.synonyms)
                adapter.updateData(wordsList = updatedPartSpeech)
            }

        }
        vm.translatorTranslationLiveData.observe(activity as LifecycleOwner) { transl ->
            transl ?: Toast.makeText(context, R.string.server_for_translation_is_not_available, Toast.LENGTH_SHORT).show()
            val text = transl ?: return@observe
            translateDialogBind.outputWord.setText(text)
            translateDialogBind.outputWord.clearFocus()
        }
    }

    private fun getLanguageFromSettings() {
        val nativeLanguageRes = R.string.russian // TODO взять язык из настроек
        val learnLanguageRes = R.string.english  // TODO взять язык из настроек

        translateDialogBind.inputLanguage.text = context.getString(learnLanguageRes)
        translateDialogBind.outputLanguage.text = context.getString(nativeLanguageRes)

        /*val config = Configuration(resources.configuration)
        config.setLocale(Locale("en"))
        val englishResources = createConfigurationContext(config).resources

        val nativeLanguage = englishResources.getString(nativeLanguageRes)
        val learnLanguage = englishResources.getString(learnLanguageRes)*/

        //this.nativeLanguage = TODO взять язык из настроек
        //this.learnLanguage = TODO взять язык из настроек
    }




    override fun onItemClick(similarWord: String, similarWordTranslate: String) {
        translateDialogBind.inputWord.setText(similarWord)
        translateDialogBind.outputWord.setText(similarWordTranslate)
    }
}