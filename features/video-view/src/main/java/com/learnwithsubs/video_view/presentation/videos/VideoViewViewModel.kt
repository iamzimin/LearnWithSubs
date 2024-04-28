package com.learnwithsubs.video_view.presentation.videos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.base.VideoConstants
import com.learnwithsubs.video_view.domain.models.DictionaryWord
import com.learnwithsubs.video_view.domain.models.Subtitle
import com.learnwithsubs.video_view.domain.models.TranslationModel
import com.learnwithsubs.video_view.domain.models.Video
import com.learnwithsubs.video_view.domain.models.WordTranslation
import com.learnwithsubs.video_view.domain.usecase.VideoViewUseCases
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
    var textToTranslate = ""

    var videoSeekBarProgress = MutableLiveData<Int>()

    var videoPlaying = MutableLiveData<Boolean>()
    var isButtonsShowedLiveData = MutableLiveData<Boolean>()

    private var subtitleList: List<Subtitle> = emptyList()

    val dictionaryWordsLiveData =  MutableLiveData<DictionaryWord>()
    val translatorTranslationLiveData = MutableLiveData<String?>()

    fun initCurrentVideo(videoId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            currentVideo = videoViewUseCases.getVideoByIdUseCase.invoke(videoId)
        }
    }

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

    fun saveWord(word: WordTranslation) {
        viewModelScope.launch {
            videoViewUseCases.saveWordUseCase.invoke(word = word)
            val vid = currentVideo ?: return@launch
            videoViewUseCases.updateVideoUseCase.invoke(video = vid.apply { saveWords++ })
        }
    }

    fun getTranslatorSource(): String {
        return videoViewUseCases.getTranslatorSource.invoke()
    }
    fun getNativeLanguage(): Pair<String, String> {
        return videoViewUseCases.getNativeLanguage.invoke()
    }
    fun getLearningLanguage(): Pair<String, String> {
        return videoViewUseCases.getLearningLanguage.invoke()
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


    fun getTranslationFromYandexDictionary(translationModel: TranslationModel) {
        viewModelScope.launch {
            val translate = videoViewUseCases.getWordsFromYandexDictionaryUseCase.invoke(model = translationModel)
            if (translate == null) {
                // TODO implement Yandex API
            } else {
                dictionaryWordsLiveData.postValue(translate ?: return@launch)
            }
        }
    }

    fun getTranslationFromServer(translationModel: TranslationModel) {
        viewModelScope.launch {
            val translate = videoViewUseCases.getTranslationFromServerUseCase.invoke(model = translationModel)
            translatorTranslationLiveData.postValue(translate)
        }
    }

    fun getTranslationFromAndroid(translationModel: TranslationModel) {
        viewModelScope.launch {
            val translate = videoViewUseCases.getTranslationFromAndroidUseCase.invoke(model = translationModel)
            translatorTranslationLiveData.postValue(translate)
        }
    }
}
