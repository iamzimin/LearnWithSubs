package com.learnwithsubs.feature_video_list.presentation.videos

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.usecase.VideoListUseCases
import com.learnwithsubs.feature_video_list.domain.util.OrderType
import com.learnwithsubs.feature_video_list.domain.util.VideoOrder
import kotlinx.coroutines.launch
import javax.inject.Inject

import android.os.Environment
import com.learnwithsubs.feature_video_list.presentation.adapter.VideoListAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import java.io.File


class VideoListViewModel @Inject constructor(
    val videoListUseCases: VideoListUseCases
) : ViewModel() {

    val videoList = MediatorLiveData<List<Video>>()
    private val currentList = videoList.value?.toMutableList() ?: mutableListOf()

    var test = MutableLiveData<Boolean>()
    init {
        updateList()
    }

    fun onEvent(event: VideosEvent) {
        when (event) {
            is VideosEvent.Order -> {
                //if (false) TODO add a check for the same choice
                //return getVideos(videoOrder = event.videoOrder)
            }
            is VideosEvent.DeleteVideo -> {
                viewModelScope.launch {
                    videoListUseCases.deleteVideoUseCase.invoke(event.video)
                }
            }
            is VideosEvent.LoadVideo -> {
                addVideo(event.video)
            }
            is VideosEvent.UpdateVideo -> {
                editVideo(event.video)
            }
        }
    }

    private fun updateList() {
        videoList.addSource(
            videoListUseCases.getVideoListUseCase.invoke(
                videoOrder = VideoOrder.Date(OrderType.Descending)
            ).asLiveData()
        ) {
            videoList.value = it
        }
    }

    private fun editVideo(video: Video) { //TODO temp for test!!!
        viewModelScope.launch {
            videoListUseCases.loadVideoUseCase.invoke(video)
        }
    }

    private fun addVideo(video: Video) {
        currentList.add(video)
        viewModelScope.launch {
            videoListUseCases.loadVideoUseCase.invoke(video)
        }


        //TODO temp for test!!!
        GlobalScope.launch {
            delay(1000)
            val videoListValue: List<Video>? = videoList.value

            if (!videoListValue.isNullOrEmpty()) {
                val lastVideo = videoListValue.first()
                /*
                val newVideo = Video(
                    id = lastVideo.id,
                    videoStatus = VideoListAdapter.NORMAL_VIDEO,
                    name = lastVideo.name,
                    preview = lastVideo.preview,
                    duration = lastVideo.duration,
                    watchProgress = lastVideo.watchProgress,
                    saveWords = lastVideo.saveWords,
                    uploadingProgress = lastVideo.uploadingProgress,
                    URI = lastVideo.URI,
                    inputPath = lastVideo.inputPath,
                    outputPath = lastVideo.outputPath,
                    timestamp = lastVideo.timestamp,
                    subtitles = lastVideo.subtitles
                )
                 */
                transcodeVideo(video = lastVideo)
            }
        }


        videoList.value = currentList.toList()
    }


    private fun transcodeVideo(video: Video) {
        val storageDir = File(Environment.getExternalStorageDirectory().toString() + "/Movies/", "LearnWithSubs")
        if (!storageDir.exists())
            storageDir.mkdirs()

        val bitrate = "2M"
        val command = "-i ${video.inputPath} -c:v mpeg4 -b:v $bitrate ${video.outputPath} -y"

        /*
        Config.enableStatisticsCallback { newStatistics ->
            Log.d(
                Config.TAG,
                String.format(
                    "frame: %d, time: %d",
                    newStatistics.videoFrameNumber,
                    newStatistics.time,
                )
            )
        }

         */
        val executionId = FFmpeg.executeAsync(command) { executionId, returnCode ->
            when (returnCode) {
                RETURN_CODE_SUCCESS -> {
                    Log.i(Config.TAG, "Async command execution completed successfully.")
                    video.videoStatus = VideoListAdapter.NORMAL_VIDEO
                    editVideo(video = video)
                }
                RETURN_CODE_CANCEL -> {
                    Log.i(Config.TAG, "Async command execution cancelled by user.")
                }
                else -> {
                    Log.i(
                        Config.TAG,
                        String.format("Async command execution failed with returnCode=$returnCode.")
                    )
                    test.value = false
                }
            }
        }

        /*
        val rc = Config.getLastReturnCode()

        when (rc) {
            RETURN_CODE_SUCCESS -> {
                Log.i(Config.TAG, "Command execution completed successfully.")
            }
            RETURN_CODE_CANCEL -> {
                Log.i(Config.TAG, "Command execution cancelled by user.")
            }
            else -> {
                Log.i(
                    Config.TAG,
                    String.format("Command execution failed with rc=$rc and the output below.")
                )
                Config.printLastCommandOutput(Log.INFO)
            }
        }

         */

    }


    /*
    fun updateList() {
        val updatedList: Flow<List<Video>> = videoListUseCases.getVideoListUseCase.invoke(
            videoOrder = VideoOrder.Date(OrderType.Descending)
        )
        videoList.value = runBlocking {
            val videoList = mutableListOf<Video>()
            updatedList.collect { videos ->
                videoList.addAll(videos)
            }
            videoList
        }
    }


    private fun getVideos(videoOrder: VideoOrder) {
        videoUseCases.getVideoListUseCase.invoke(videoOrder = videoOrder)
            .onEach { videos: List<Video> -> }
    }*/
}