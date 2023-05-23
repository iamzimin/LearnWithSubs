package com.learnwithsubs.feature_video_list.presentation.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.R
import com.learnwithsubs.feature_video_list.domain.models.Video

class VideoAdapter(videoListInit: ArrayList<Video>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var videoList: ArrayList<Video> = videoListInit

    companion object {
        const val NORMAL_VIDEO = 1
        const val SELECTED_VIDEO = 2
        const val LOADING_VIDEO = 3
    }

    fun updateData(videoList: ArrayList<Video>) {
        this@VideoAdapter.videoList = videoList
        notifyDataSetChanged() //TODO передавать в качестве агрументов id обновлённого видоса
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

        when (videoList[position].videoStatus) {
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
        return videoList[position].videoStatus
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