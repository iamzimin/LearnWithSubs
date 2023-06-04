package com.learnwithsubs.feature_video_list.presentation.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.learnwithsubs.R
import com.learnwithsubs.feature_video_list.domain.models.Video
import com.learnwithsubs.feature_video_list.domain.models.VideoStatus


class VideoListAdapter(
    videoListInit: ArrayList<Video>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var videoList: ArrayList<Video> = videoListInit
    private var videoSelected = ArrayList<Video>()
    var isNormalMode = true

    fun updateData(videoList: ArrayList<Video>) {
        this@VideoListAdapter.videoList = videoList
        videoSelected.forEach { selectedVideo ->
            val videoToUpdate = videoList.find { it.id == selectedVideo.id }
            videoToUpdate?.isSelected = selectedVideo.isSelected
        }
        notifyDataSetChanged() //TODO передавать в качестве агрументов id обновлённого видоса
    }

    fun updateVideo(videoToUpdate: Video) {
        var position: Int? = null
        for (i in 0 until this@VideoListAdapter.videoList.size) {
            val video = this@VideoListAdapter.videoList[i]
            if (video.id == videoToUpdate.id) {
                this@VideoListAdapter.videoList[i] = videoToUpdate
                position = i
                break
            }
        }
        if (position != null)
            notifyItemChanged(position)
    }

    fun updateSelection(position: Int) {
        val video = videoList[position]
        video.isSelected = !video.isSelected
        if (video.isSelected)
            videoSelected.add(video)
        else
            videoSelected.remove(videoSelected.find { it.id == video.id })
        isNormalMode = videoSelected.isEmpty()

        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VideoStatus.NORMAL_VIDEO.value -> NormalVideoViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.video_tile, parent, false), this
            )

            VideoStatus.LOADING_VIDEO.value -> LoadingVideoViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.video_uploading_tile, parent, false), this
            )

            else -> {
                LoadingVideoViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.video_uploading_tile, parent, false), this
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val video = videoList[position]
        val context = holder.itemView.context

        when (video.videoStatus) {
            VideoStatus.NORMAL_VIDEO -> {
                val normalHolder = holder as NormalVideoViewHolder
                normalHolder.bind(videoList[position])
            }

            VideoStatus.LOADING_VIDEO -> {
                val loadingHolder = holder as LoadingVideoViewHolder
                loadingHolder.bind(videoList[position])
            }
        }
        if (video.isSelected) {
            val color = ContextCompat.getColor(context, R.color.background_video_selected_tile_dark) // TODO цвета берутся не с attr
            (holder.itemView as MaterialCardView).setCardBackgroundColor(color)
        }
        else {
            val color = ContextCompat.getColor(context, R.color.background_video_tile_dark)
            (holder.itemView as MaterialCardView).setCardBackgroundColor(color)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return videoList[position].videoStatus.value
    }

    class RecyclerViewItemDecoration(private val spaceSize: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.left = spaceSize
            outRect.right = spaceSize
            outRect.bottom = spaceSize
            outRect.top = spaceSize
        }
    }
}