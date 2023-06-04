package com.learnwithsubs.feature_video_list.presentation.adapter

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.R
import com.learnwithsubs.databinding.VideoSelectedTileBinding
import com.learnwithsubs.databinding.VideoTileBinding
import com.learnwithsubs.databinding.VideoUploadingTileBinding
import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.models.VideoLoadingType
import com.learnwithsubs.feature_video_list.domain.models.VideoStatus
import com.learnwithsubs.feature_video_view.presentation.VideoViewActivity
import java.util.concurrent.TimeUnit

// Родительский класс для всех видео ViewHolder
abstract class VideoViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    protected val duration: String = itemView.context.getString(R.string.video_duration)
    protected val savedWords: String = itemView.context.getString(R.string.video_saved_words)
    protected val videoIsUploading: String = itemView.context.getString(R.string.video_is_uploading)

    protected fun formatDuration(duration: Int): String {
        val hours = TimeUnit.MILLISECONDS.toHours(duration.toLong())
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration.toLong()) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    abstract fun bind(video: Video)
}

// ViewHolder для обычного видео
class NormalVideoViewHolder(
    itemView: View,
    private val adapter: VideoListAdapter
) : VideoViewHolder(itemView) {
    private val binding = VideoTileBinding.bind(itemView)

    override fun bind(video: Video) {
        binding.videoName.text = video.name
        binding.duration.text = "${duration}: ${formatDuration(video.duration)}"
        binding.savedWords.text = "${savedWords}: ${video.saveWords}"
        //binding.videoPreview.setImageResource(video.preview)
        binding.videoProgress.progress = ((video.watchProgress / video.duration.toDouble()) * 100).toInt()

        itemView.setOnClickListener (object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (adapter.isNormalMode) {
                    val intent = Intent(itemView.context, VideoViewActivity::class.java)
                    intent.putExtra("videoData", video)
                    itemView.context.startActivity(intent)
                }
                else {
                    val position = adapterPosition
                    adapter.updateSelection(position)
                }
            }
        })

        itemView.setOnLongClickListener {
            val position = adapterPosition
            adapter.updateSelection(position)
            true
        }
    }
}

// ViewHolder для видео в статусе "загрузка"
class LoadingVideoViewHolder(
    itemView: View,
    private val adapter: VideoListAdapter
) : VideoViewHolder(itemView) {
    private val binding = VideoUploadingTileBinding.bind(itemView)

    override fun bind(video: Video) {
        binding.videoName.text = video.name
        binding.duration.text = "${duration}: ${formatDuration(video.duration)}"

        binding.progressVideoLoadingText.text =
            if (video.uploadingProgress == 0) "" else video.uploadingProgress.toString()
        val status = "${itemView.context.getString(R.string.video_loading_status)}: "
        when (video.loadingType) {
            VideoLoadingType.WAITING ->
                binding.loadingStatus.text = status + itemView.context.getString(R.string.video_loading_status_waiting)
            VideoLoadingType.EXTRACTING_AUDIO ->
                binding.loadingStatus.text = status + itemView.context.getString(R.string.video_loading_status_extracting_audio)
            VideoLoadingType.DECODING_VIDEO ->
                binding.loadingStatus.text = status + itemView.context.getString(R.string.video_loading_status_decoding_video)
            VideoLoadingType.LOADING_AUDIO ->
                binding.loadingStatus.text = status + itemView.context.getString(R.string.video_loading_status_loading_audio)
            VideoLoadingType.GENERATING_SUBTITLES ->
                binding.loadingStatus.text = status + itemView.context.getString(R.string.video_loading_status_generating_subtitles)
            VideoLoadingType.DONE ->
                binding.loadingStatus.text = status + itemView.context.getString(R.string.video_loading_status_done)
        }

        //binding.videoPreview.setImageResource(video.preview)
        itemView.setOnClickListener (object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (adapter.isNormalMode)
                    Toast.makeText(itemView.context.applicationContext, videoIsUploading, Toast.LENGTH_SHORT).show()
                else {
                    val position = adapterPosition
                    adapter.updateSelection(position)
                }
            }

        })

        itemView.setOnLongClickListener {
            val position = adapterPosition
            adapter.updateSelection(position)
            true
        }
    }
}
