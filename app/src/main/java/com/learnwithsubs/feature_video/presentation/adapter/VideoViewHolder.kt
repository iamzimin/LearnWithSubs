package com.learnwithsubs.feature_video.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.R
import com.learnwithsubs.databinding.VideoSelectedTileBinding
import com.learnwithsubs.databinding.VideoTileBinding
import com.learnwithsubs.databinding.VideoUploadingTileBinding
import com.learnwithsubs.feature_video.domain.models.Video
import java.util.concurrent.TimeUnit

// Родительский класс для всех видео ViewHolder
abstract class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    protected val duration: String = itemView.context.getString(R.string.video_duration)
    protected val savedWords: String = itemView.context.getString(R.string.video_saved_words)

    protected fun formatDuration(duration: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(duration)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    abstract fun bind(video: Video)
}

// ViewHolder для обычного видео
class NormalVideoViewHolder(itemView: View) : VideoViewHolder(itemView) {
    private val binding = VideoTileBinding.bind(itemView)

    override fun bind(video: Video) {
        binding.videoName.text = video.name
        binding.duration.text = "${duration}: ${formatDuration(video.duration)}"
        binding.savedWords.text = "${savedWords}: ${video.saveWords}"
        //binding.videoPreview.setImageResource(video.preview)
        binding.videoProgress.progress = 88
    }
}

// ViewHolder для выбранного видео
class SelectedVideoViewHolder(itemView: View) : VideoViewHolder(itemView) {
    private val binding = VideoSelectedTileBinding.bind(itemView)

    override fun bind(video: Video) {
        binding.videoName.text = video.name
        binding.duration.text = "${duration}: ${formatDuration(video.duration)}"
        binding.savedWords.text = "${savedWords}: ${video.saveWords}"
        //binding.videoPreview.setImageResource(video.preview)
        binding.videoProgress.progress = 88
    }
}

// ViewHolder для видео в статусе "загрузка"
class LoadingVideoViewHolder(itemView: View) : VideoViewHolder(itemView) {
    private val binding = VideoUploadingTileBinding.bind(itemView)

    override fun bind(video: Video) {
        binding.videoName.text = video.name
        binding.duration.text = "${duration}: ${formatDuration(video.duration)}"
        binding.progressVideoLoading.progress = video.uploadingProgress
        //binding.videoPreview.setImageResource(video.preview)
    }
}
