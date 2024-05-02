package com.learnwithsubs.video_list.presentation.adapter

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.base.VideoConstants
import com.learnwithsubs.resource.R
import com.learnwithsubs.video_list.databinding.TileVideoBinding
import com.learnwithsubs.video_list.databinding.TileVideoUploadingBinding
import com.learnwithsubs.video_list.domain.models.Video
import com.learnwithsubs.video_list.domain.models.VideoLoadingType
import java.io.File
import java.util.concurrent.TimeUnit

// Родительский класс для всех видео ViewHolder
abstract class VideoViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    protected val duration: String = itemView.context.getString(R.string.video_duration)
    protected val savedWords: String = itemView.context.getString(R.string.video_saved_words)
    protected val videoIsUploading: String = itemView.context.getString(R.string.video_is_uploading)

    protected fun formatDuration(duration: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(duration)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60L
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60L

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    abstract fun bind(video: Video, isSelected: Boolean)
}

// ViewHolder для обычного видео
class NormalVideoViewHolder(
    itemView: View,
    private val adapter: VideoListAdapter
) : VideoViewHolder(itemView) {
    private val binding = TileVideoBinding.bind(itemView)

    override fun bind(video: Video, isSelected: Boolean) {
        binding.videoName.text = video.name
        binding.duration.text = "${duration}: ${formatDuration(video.duration)}"
        binding.savedWords.text = "${savedWords}: ${video.saveWords}"
        binding.selectCheckBox.isChecked = isSelected
        binding.videoPreview.load(File(video.outputPath, VideoConstants.VIDEO_PREVIEW).absoluteFile) {
            transformations(RoundedCornersTransformation(10f))
            error(R.drawable.rectangle)
        }
        binding.videoProgress.progress = ((video.watchProgress / video.duration.toDouble()) * 100).toInt()

        if (adapter.getIsSelectionMode())
            binding.selectCheckBox.visibility = View.VISIBLE
        else
            binding.selectCheckBox.visibility = View.GONE

        itemView.setOnClickListener (object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (adapter.getIsSelectionMode()) {
                    val position = adapterPosition
                    binding.selectCheckBox.isChecked = !binding.selectCheckBox.isChecked
                    adapter.updateSelection(position = position, isSelected = binding.selectCheckBox.isChecked)
                } else {
                    val deeplink = Uri.parse("app://learnwithsubs/video_view")
                    val intent = Intent(Intent.ACTION_VIEW, deeplink)
                    intent.setPackage("com.learnwithsubs")
                    intent.putExtra("videoID", video.id)
                    itemView.context.startActivity(intent)
                }
            }
        })

        itemView.setOnLongClickListener {
            val position = adapterPosition
            binding.selectCheckBox.isChecked = !binding.selectCheckBox.isChecked
            adapter.updateSelection(position = position, isSelected = binding.selectCheckBox.isChecked)
            if (!adapter.getIsSelectionMode())
                adapter.changeMode(isSelectionMode = true)
            true
        }
    }
}

// ViewHolder для видео в статусе "загрузка"
class LoadingVideoViewHolder(
    itemView: View,
    private val adapter: VideoListAdapter
) : VideoViewHolder(itemView) {
    private val binding = TileVideoUploadingBinding.bind(itemView)

    override fun bind(video: Video, isSelected: Boolean) {
        binding.videoName.text = video.name
        binding.selectCheckBox.isChecked = isSelected
        binding.duration.text = "${duration}: ${formatDuration(video.duration)}"

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
        binding.progressVideoLoadingText.text = if (video.uploadingProgress == 0) "" else video.uploadingProgress.toString()
        if (video.loadingType == VideoLoadingType.GENERATING_SUBTITLES) {
            binding.progressVideoLoading.isIndeterminate = true
        }
        else {
            binding.progressVideoLoading.isIndeterminate = false
            binding.progressVideoLoading.progress = video.uploadingProgress
        }

        if (adapter.getIsSelectionMode())
            binding.selectCheckBox.visibility = View.VISIBLE
        else
            binding.selectCheckBox.visibility = View.GONE

        itemView.setOnClickListener (object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (adapter.getIsSelectionMode()) {
                    val position = adapterPosition
                    binding.selectCheckBox.isChecked = !binding.selectCheckBox.isChecked
                    adapter.updateSelection(position = position, isSelected = binding.selectCheckBox.isChecked)
                } else {
                    Toast.makeText(itemView.context.applicationContext, videoIsUploading, Toast.LENGTH_SHORT).show()
                }
            }

        })

        itemView.setOnLongClickListener {
            val position = adapterPosition
            binding.selectCheckBox.isChecked = !binding.selectCheckBox.isChecked
            adapter.updateSelection(position = position, isSelected = binding.selectCheckBox.isChecked)
            if (!adapter.getIsSelectionMode())
                adapter.changeMode(isSelectionMode = true)
            true
        }
    }
}
