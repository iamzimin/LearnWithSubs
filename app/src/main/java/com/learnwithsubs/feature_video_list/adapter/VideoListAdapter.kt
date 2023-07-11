package com.learnwithsubs.feature_video_list.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.R
import com.learnwithsubs.SelectableAdapter
import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.models.VideoStatus


class VideoListAdapter: SelectableAdapter<Video>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VideoStatus.NORMAL_VIDEO.value -> NormalVideoViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.tile_video, parent, false), this@VideoListAdapter
            )

            VideoStatus.LOADING_VIDEO.value -> LoadingVideoViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.tile_video_uploading, parent, false), this@VideoListAdapter
            )

            else -> {
                LoadingVideoViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.tile_video_uploading, parent, false), this@VideoListAdapter
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val video = itemList[position]

        when (video.videoStatus) {
            VideoStatus.NORMAL_VIDEO -> {
                val normalHolder = holder as NormalVideoViewHolder
                val isSelected = selectedItems.any { it.id == itemList[position].id }
                normalHolder.bind(itemList[position], isSelected)
            }

            VideoStatus.LOADING_VIDEO -> {
                val loadingHolder = holder as LoadingVideoViewHolder
                val isSelected = selectedItems.any { it.id == itemList[position].id }
                loadingHolder.bind(itemList[position], isSelected)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return itemList[position].videoStatus.value
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