package com.learnwithsubs.video_view.presentation.videos

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnwithsubs.R
import com.learnwithsubs.video_list.domain.VideoConstants
import com.learnwithsubs.video_list.domain.models.Video
import com.learnwithsubs.word_list.domain.models.WordTranslation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class VideoViewViewModel @Inject constructor(
    private val videoViewUseCases: com.learnwithsubs.video_view.domain.usecase.VideoViewUseCases
) : ViewModel() {
    var currentVideo: com.learnwithsubs.video_list.domain.models.Video? = null

    val videoPath = MutableLiveData<String>()
    val videoName = MutableLiveData<String>()
    val subtitleError = MutableLiveData<String>()

    private var maxVideoTime: Long = 0L
    private var maxTimeString: String = ""
    private var currentVideoWatchTime: Int = 0
    var videoTime = MutableLiveData<String>()

    var videoSeekBarProgress = MutableLiveData<Int>()

    var videoPlaying = MutableLiveData<Boolean>()
    var isButtonsShowedLiveData = MutableLiveData<Boolean>()

    private var subtitleList: List<com.learnwithsubs.video_view.domain.models.Subtitle> = emptyList()

    val dictionaryWordsLiveData =  MutableLiveData<com.example.yandex_dictionary_api.models.DictionaryWord?>()
    val translatorTranslationLiveData = MutableLiveData<String?>()


    fun openVideo(video: com.learnwithsubs.video_list.domain.models.Video, isPlaying: Boolean) {
        val subList = videoViewUseCases.getVideoSubtitlesUseCase.invoke(video)
        if (subList == null)
            subtitleError.value = ""

        subtitleList = subList ?: emptyList()

        videoPath.value = File(video.outputPath, com.learnwithsubs.video_list.domain.VideoConstants.COPIED_VIDEO).absolutePath
        videoName.value = video.name
        maxVideoTime = video.duration
        maxTimeString = formatTime(time = maxVideoTime)
        videoPlaying.value = isPlaying
        isButtonsShowedLiveData.value = true
    }


    fun saveVideo(video: com.learnwithsubs.video_list.domain.models.Video) {
        video.watchProgress = currentVideoWatchTime
        viewModelScope.launch(Dispatchers.IO) {
            videoViewUseCases.updateVideoUseCase.invoke(video = video)
        }
    }

    fun saveWord(word: com.learnwithsubs.word_list.domain.models.WordTranslation) {
        viewModelScope.launch {
            videoViewUseCases.saveWordUseCase.invoke(word = word)
            val vid = currentVideo ?: return@launch
            videoViewUseCases.updateVideoUseCase.invoke(video = vid.apply { saveWords++ })
        }
    }


    fun updateCurrentTime(currTime: Int) {
        currentVideoWatchTime = currTime
        val time = formatTime(time = currTime.toLong()) + " / $maxTimeString"
        videoTime.value = time
        videoSeekBarProgress.value = (((currTime.toLong() / maxVideoTime.toDouble())) * 100).toInt()
    }

    fun getCurrentSubtitles(time: Long): String {
        val currentSubtitle = subtitleList.find { subtitle ->
            time >= subtitle.startTime && time <= subtitle.endTime
        }
        return currentSubtitle?.text ?: ""
    }


    private fun formatTime(time: Long): String {
        val currHours = time / 1000L / 3600L
        val currMinutes = time / 1000L / 60L % 60L
        val currSeconds = time / 1000L % 60L

        return if (currHours > 0)
            String.format("%02d:%02d:%02d", currHours, currMinutes, currSeconds)
        else
            String.format("%02d:%02d", currMinutes, currSeconds)
    }

    fun getWordsFromDictionary(word: String, inputLang: String, outputLang: String) {
        val translationModel = com.learnwithsubs.video_view.domain.models.TranslationModel(
            word = word,
            inputLanguage = inputLang,
            outputLanguage = outputLang,
        )
        viewModelScope.launch {
            val translate = videoViewUseCases.getWordsFromYandexDictionaryUseCase.invoke(model = translationModel)
            dictionaryWordsLiveData.postValue(translate)
        }
    }

    fun getFullTranslation(word: String, inputLang: String, outputLang: String) {
        val translationModel = com.learnwithsubs.video_view.domain.models.TranslationModel(
            word = word,
            inputLanguage = inputLang,
            outputLanguage = outputLang,
        )
        if (false) { //TODO взять настроек
            getWordsFromServerTranslator(translationModel = translationModel)
        } else {
            getWordsFromAndroidTranslator(translationModel = translationModel)
        }
    }

    private fun getWordsFromServerTranslator(translationModel: com.learnwithsubs.video_view.domain.models.TranslationModel) {
        viewModelScope.launch {
            val translate = videoViewUseCases.getTranslationFromServerUseCase.invoke(model = translationModel)
            translatorTranslationLiveData.postValue(translate)
        }
    }

    private fun getWordsFromAndroidTranslator(translationModel: com.learnwithsubs.video_view.domain.models.TranslationModel) {
        viewModelScope.launch {
            val translate = videoViewUseCases.getTranslationFromAndroidUseCase.invoke(model = translationModel)
            translatorTranslationLiveData.postValue(translate)
        }
    }

    fun changePartSpeech(context: Context, list: ArrayList<com.example.yandex_dictionary_api.models.DictionarySynonyms>):  ArrayList<com.example.yandex_dictionary_api.models.DictionarySynonyms> {
        for (elem in list) {
            when (elem.partSpeech) {
                "noun" -> elem.partSpeech = context.getString(R.string.noun)
                "adjective" -> elem.partSpeech = context.getString(R.string.adjective)
                "adverb" -> elem.partSpeech = context.getString(R.string.adverb)
                "participle" -> elem.partSpeech = context.getString(R.string.participle)
                "predicative" -> elem.partSpeech = context.getString(R.string.predicative)

                else -> {}
            }
        }
        return list
    }

}