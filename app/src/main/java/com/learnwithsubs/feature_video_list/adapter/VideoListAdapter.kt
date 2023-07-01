package com.learnwithsubs.feature_video_list.adapter

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.learnwithsubs.R
import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.models.VideoStatus


class VideoListAdapter(
    videoListInit: ArrayList<Video>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var videoList: ArrayList<Video> = videoListInit
    private var videoSelected = ArrayList<Video>()
    private var isNormalMode = true


    fun updateData(videoList: ArrayList<Video>) {
        val selectedList = getSelected(newList = videoList)
        val diffResult = DiffUtil.calculateDiff(VideoDiffCallback(this.videoList, selectedList))
        this.videoList = selectedList
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateVideo(videoToUpdate: Video) {
        val position = this@VideoListAdapter.videoList.indexOfFirst { it.id == videoToUpdate.id }
        if (position != -1) {
            this@VideoListAdapter.videoList[position] = videoToUpdate
            notifyItemChanged(position)
        }
    }

    fun updateSelection(position: Int) {
        videoList.getOrNull(position)?.let { video ->
            video.isSelected = !video.isSelected
            if (video.isSelected)
                videoSelected.add(video)
            else
                videoSelected.remove(videoSelected.find { it.id == video.id })
            isNormalMode = videoSelected.isEmpty()

            notifyItemChanged(position)
        }
    }


    private fun getSelected(newList: ArrayList<Video>): ArrayList<Video> {
        for (video in newList) {
            val found = videoSelected.find { it.id == video.id }
            if (found != null) video.isSelected = found.isSelected
        }
        videoSelected.clear()
        videoSelected.addAll(newList.filter { it.isSelected })
        isNormalMode = videoSelected.isEmpty()
        return newList
    }

    fun selectClear() {
        videoSelected.clear()
        isNormalMode = true
    }
    fun selectAll() {
        videoSelected = ArrayList(videoList)
        isNormalMode = false
    }
    fun getVideoListSize(): Int {
        return videoList.size
    }
    fun getVideoSelectedSize(): Int {
        return videoSelected.size
    }
    fun getEditableVideo(): Video? {
        return if (videoSelected.size == 1) videoSelected[0]
        else null
    }
    fun getIsNormalMode(): Boolean {
        return isNormalMode
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
        if (video.isSelected)
            (holder.itemView as MaterialCardView).setCardBackgroundColor(
                context.getColorFromAttr(R.attr.background_video_selected_tile_dark)
            )
        else
            (holder.itemView as MaterialCardView).setCardBackgroundColor(
                context.getColorFromAttr(R.attr.background_video_tile_dark)
            )
    }

    private fun Context.getColorFromAttr(attrId: Int): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(attrId, typedValue, true)
        return typedValue.data
    }

    override fun getItemViewType(position: Int): Int {
        return videoList[position].videoStatus.value
    }

    class RecyclerViewItemDecoration(private val spaceSize: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.left = spaceSize
            outRect.right = spaceSize
            outRect.bottom = spaceSize
            outRect.top = spaceSize
        }
    }

    class VideoDiffCallback(private val oldList: List<Video>, private val newList: List<Video>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}