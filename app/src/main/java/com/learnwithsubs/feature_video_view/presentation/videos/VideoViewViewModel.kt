package com.learnwithsubs.feature_video_view.presentation.videos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_view.domain.models.DictionaryWord
import com.learnwithsubs.feature_video_view.domain.models.Subtitle
import com.learnwithsubs.feature_video_view.domain.usecase.VideoViewUseCases
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

    val dictionaryListLiveData = videoViewUseCases.getWordsFromDictionaryUseCase.dictionaryListLiveData
    val translationLiveData = videoViewUseCases.getWordsFromDictionaryUseCase.translationLiveData
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

    fun getWordsFromDictionary(key: String, lang: String, word: String): ArrayList<DictionaryWord> {
        return ArrayList(videoViewUseCases.getWordsFromDictionaryUseCase.invoke(key, lang, word))
    }

}
