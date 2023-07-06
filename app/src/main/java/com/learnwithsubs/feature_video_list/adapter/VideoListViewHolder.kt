package com.learnwithsubs.feature_video_list.adapter

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.learnwithsubs.R
import com.learnwithsubs.databinding.VideoTileBinding
import com.learnwithsubs.databinding.VideoUploadingTileBinding
import com.learnwithsubs.feature_video_list.VideoConstants
import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.models.VideoLoadingType
import com.learnwithsubs.feature_video_view.VideoViewActivity
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
    private val binding = VideoTileBinding.bind(itemView)

    override fun bind(video: Video, isSelected: Boolean) {
        binding.videoName.text = video.name
        binding.duration.text = "${duration}: ${formatDuration(video.duration)}"
        binding.savedWords.text = "${savedWords}: ${video.saveWords}"
        binding.videoSelectCheckBox.isChecked = isSelected
        binding.videoPreview.load(File(video.outputPath, VideoConstants.VIDEO_PREVIEW).absoluteFile) {
            transformations(RoundedCornersTransformation(10f))
            error(R.drawable.rectangle)
        }
        binding.videoProgress.progress = ((video.watchProgress / video.duration.toDouble()) * 100).toInt()

        if (adapter.getIsNormalMode())
            binding.videoSelectCheckBox.visibility = View.GONE
        else
            binding.videoSelectCheckBox.visibility = View.VISIBLE

        itemView.setOnClickListener (object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (adapter.getIsNormalMode()) {
                    val intent = Intent(itemView.context, VideoViewActivity::class.java)
                    intent.putExtra("videoData", video)
                    itemView.context.startActivity(intent)
                }
                else {
                    val position = adapterPosition
                    binding.videoSelectCheckBox.isChecked = !binding.videoSelectCheckBox.isChecked
                    adapter.updateSelection(position = position, isSelected = binding.videoSelectCheckBox.isChecked)
                }
            }
        })

        itemView.setOnLongClickListener {
            val position = adapterPosition
            binding.videoSelectCheckBox.isChecked = !binding.videoSelectCheckBox.isChecked
            adapter.updateSelection(position = position, isSelected = binding.videoSelectCheckBox.isChecked)
            if (adapter.getIsNormalMode())
                adapter.changeMode(isNormalMode = false)
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

    override fun bind(video: Video, isSelected: Boolean) {
        binding.videoName.text = video.name
        binding.videoSelectCheckBox.isChecked = isSelected
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

        if (adapter.getIsNormalMode())
            binding.videoSelectCheckBox.visibility = View.GONE
        else
            binding.videoSelectCheckBox.visibility = View.VISIBLE

        itemView.setOnClickListener (object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (adapter.getIsNormalMode())
                    Toast.makeText(itemView.context.applicationContext, videoIsUploading, Toast.LENGTH_SHORT).show()
                else {
                    val position = adapterPosition
                    binding.videoSelectCheckBox.isChecked = !binding.videoSelectCheckBox.isChecked
                    adapter.updateSelection(position = position, isSelected = binding.videoSelectCheckBox.isChecked)
                }
            }

        })

        itemView.setOnLongClickListener {
            val position = adapterPosition
            binding.videoSelectCheckBox.isChecked = !binding.videoSelectCheckBox.isChecked
            adapter.updateSelection(position = position, isSelected = binding.videoSelectCheckBox.isChecked)
            if (adapter.getIsNormalMode())
                adapter.changeMode(isNormalMode = false)
            true
        }
    }
}
