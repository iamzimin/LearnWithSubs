package com.learnwithsubs.feature_video_view.videos

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnwithsubs.R
import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_view.models.DictionaryWord
import com.learnwithsubs.feature_video_view.models.Subtitle
import com.learnwithsubs.feature_video_view.usecase.VideoViewUseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoViewViewModel @Inject constructor(
    val videoViewUseCases: VideoViewUseCases
) : ViewModel() {
    var currentVideo: MutableLiveData<Video> = MutableLiveData<Video>()

    var videoPath = MutableLiveData<String>()
    var videoName = MutableLiveData<String>()

    private var maxVideoTime: Int = 0
    private var maxTimeString: String = ""
    private var currentVideoWatchTime: Int = 0
    var videoTime = MutableLiveData<String>()

    var videoSeekBarProgress = MutableLiveData<Int>()

    var videoPlaying = MutableLiveData<Boolean>()
    var isButtonsShowedLiveData = MutableLiveData<Boolean>()

    private var subtitleList: List<Subtitle> = emptyList()

    var nativeLanguage = ""
    var learnLanguage = ""
    val dictionarySynonymsLiveData = videoViewUseCases.getWordsFromDictionaryUseCase.dictionaryListLiveData
    val dictionaryTranslationLiveData = videoViewUseCases.getWordsFromDictionaryUseCase.translationLiveData
    val translatorTranslationLiveData = MutableLiveData<String?>()
    var textToTranslate: String = ""


    fun openVideo(video: Video) {
        subtitleList = videoViewUseCases.getVideoSubtitlesUseCase.invoke(currentVideo.value)

        videoPath.value = "${video.outputPath}.mp4"
        videoName.value = video.name
        maxVideoTime = video.duration
        maxTimeString = formatTime(time = maxVideoTime)
        videoPlaying.value = true
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
        val time = formatTime(time = currTime) + " / $maxTimeString"
        videoTime.value = time
        videoSeekBarProgress.value = (((currTime / maxVideoTime.toDouble())) * 100).toInt()
    }

    fun getCurrentSubtitles(time: Long): String {
        val currentSubtitle = subtitleList.find { subtitle ->
            time >= subtitle.startTime && time <= subtitle.endTime
        }
        return currentSubtitle?.text ?: ""
    }


    private fun formatTime(time: Int): String {
        val currHours = time / 1000 / 3600
        val currMinutes = time / 1000 / 60 % 60
        val currSeconds = time / 1000 % 60

        return if (currHours > 0)
            String.format("%02d:%02d:%02d", currHours, currMinutes, currSeconds)
        else
            String.format("%02d:%02d", currMinutes, currSeconds)
    }

    fun getWordsFromDictionary(key: String, inputLang: String, outputLang: String, word: String) {
        val inputLangPair = Pair(inputLang, inputLang.substring(0, 2).lowercase())
        val outputLangPair = Pair(outputLang, outputLang.substring(0, 2).lowercase())
        videoViewUseCases.getWordsFromDictionaryUseCase.invoke(key, inputLangPair, outputLangPair, word)
    }

    fun getWordsFromTranslator(word: String, learnLanguage: String) {
        val outputLangPair = Pair(learnLanguage, learnLanguage.substring(0, 2).lowercase())
        viewModelScope.launch {
            val translate = videoViewUseCases.getTranslationUseCase.invoke(word = word, learnLanguage = outputLangPair)
            translatorTranslationLiveData.postValue(translate)
        }
    }

    fun changePartSpeech(context: Context, list: ArrayList<DictionaryWord>): ArrayList<DictionaryWord> {
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
