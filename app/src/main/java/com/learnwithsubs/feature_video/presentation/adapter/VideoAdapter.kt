package com.learnwithsubs.feature_video.presentation.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.learnwithsubs.R
import com.learnwithsubs.databinding.VideoSelectedTileBinding
import com.learnwithsubs.databinding.VideoTileBinding
import com.learnwithsubs.databinding.VideoUploadingTileBinding
import com.learnwithsubs.feature_video.data.storage.VideoData

class VideoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val NORMAL_VIDEO = 1
        const val SELECTED_VIDEO = 2
        const val LOADING_VIDEO = 3
    }

    var videoList = ArrayList<VideoData>()

    fun addVideo(id: Int) {
        val video = VideoData(id,"123", 1, 2,4, 5)
        videoList.add(video)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            NORMAL_VIDEO -> NormalVideoViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.video_tile, parent, false)
            )
            SELECTED_VIDEO -> SelectedVideoViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.video_selected_tile, parent, false)
            )
            LOADING_VIDEO -> LoadingVideoViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.video_uploading_tile, parent, false)
            )
            else -> {LoadingVideoViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.video_uploading_tile, parent, false))
            }
        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (videoList[position].videoType) {
            NORMAL_VIDEO -> {
                val normalHolder = holder as NormalVideoViewHolder
                normalHolder.bind(videoList[position])
            }
            SELECTED_VIDEO -> {
                val selectedHolder = holder as SelectedVideoViewHolder
                selectedHolder.bind(videoList[position])
            }
            LOADING_VIDEO -> {
                val loadingHolder = holder as LoadingVideoViewHolder
                loadingHolder.bind(videoList[position])
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return videoList[position].videoType
    }



    // ViewHolder для обычного видео
    class NormalVideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = VideoTileBinding.bind(itemView)
        fun bind(video: VideoData) = with(binding) {
                binding.videoName.text = "VideoName"
                binding.duration.text = "Duration: 1:23:45"
                binding.savedWords.text = "Saved words: 789"
                //binding.videoPreview.setImageResource(video.preview)
                binding.videoProgress.progress = 88
            }
        }
    // ViewHolder для выбранного видео
    class SelectedVideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = VideoSelectedTileBinding.bind(itemView)
        fun bind(video: VideoData) = with(binding) {
            binding.videoName.text = "VideoName"
            binding.duration.text = "Duration: 1:23:45"
            binding.savedWords.text = "Saved words: 789"
            //binding.videoPreview.setImageResource(video.preview)
            binding.videoProgress.progress = 88
        }
    }

    // ViewHolder для видео в статусе "загрузка"
    class LoadingVideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = VideoUploadingTileBinding.bind(itemView)
        fun bind(video: VideoData) = with(binding) {
            binding.videoName.text = "VideoName"
            binding.duration.text = "Duration: 1:23:45"
            //binding.videoPreview.setImageResource(video.preview)
        }
    }


    class RecyclerViewItemDecoration(private val spaceSize: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.left = spaceSize
            outRect.right = spaceSize
            outRect.bottom = spaceSize
            outRect.top = spaceSize
        }
    }
}