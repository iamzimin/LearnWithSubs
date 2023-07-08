package com.learnwithsubs.feature_video_view.videos

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnwithsubs.R
import com.learnwithsubs.feature_video_list.VideoConstants
import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_view.models.DictionaryModel
import com.learnwithsubs.feature_video_view.models.DictionarySynonyms
import com.learnwithsubs.feature_video_view.models.DictionaryWord
import com.learnwithsubs.feature_video_view.models.Subtitle
import com.learnwithsubs.feature_video_view.models.TranslationModel
import com.learnwithsubs.feature_video_view.usecase.VideoViewUseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class VideoViewViewModel @Inject constructor(
    private val videoViewUseCases: VideoViewUseCases
) : ViewModel() {
    var currentVideo: Video? = null

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

    private var subtitleList: List<Subtitle> = emptyList()

    val dictionaryWordsLiveData =  MutableLiveData<DictionaryWord?>()
    val translatorTranslationLiveData = MutableLiveData<String?>()


    fun openVideo(video: Video, isPlaying: Boolean) {
        val subList = videoViewUseCases.getVideoSubtitlesUseCase.invoke(video)
        if (subList == null)
            subtitleError.value = ""

        subtitleList = subList ?: emptyList()

        videoPath.value = File(video.outputPath, VideoConstants.COPIED_VIDEO).absolutePath
        videoName.value = video.name
        maxVideoTime = video.duration
        maxTimeString = formatTime(time = maxVideoTime)
        videoPlaying.value = isPlaying
        isButtonsShowedLiveData.value = true
    }


    fun saveVideo(video: Video) {
        video.watchProgress = currentVideoWatchTime
        viewModelScope.launch(Dispatchers.IO) {
            videoViewUseCases.updateVideoUseCase.invoke(video = video)
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
        val dictionaryModel = DictionaryModel(
            word = word,
            inputLanguage = inputLang,
            inputLanguage_ISO639_1 = languageToISO6391(inputLang),
            outputLanguage = outputLang,
            outputLanguage_ISO639_1 = languageToISO6391(outputLang)
        )
        viewModelScope.launch {
            val translate = videoViewUseCases.getWordsFromYandexDictionaryUseCase.invoke(model = dictionaryModel)
            dictionaryWordsLiveData.postValue(translate)
        }
    }

    fun getWordsFromTranslator(word: String, learnLanguage: String) {
        val translationModel = TranslationModel(
            word = word,
            learnLanguage = learnLanguage,
            learnLanguage_ISO639_1 = languageToISO6391(learnLanguage)
        )
        viewModelScope.launch {
            val translate = videoViewUseCases.getYandexTranslationUseCase.invoke(model = translationModel)
            translatorTranslationLiveData.postValue(translate)
        }
    }

    fun languageToISO6391(language: String): String {
        return language.substring(0, 2).lowercase()
    }

    fun changePartSpeech(context: Context, list: ArrayList<DictionarySynonyms>):  ArrayList<DictionarySynonyms> {
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
