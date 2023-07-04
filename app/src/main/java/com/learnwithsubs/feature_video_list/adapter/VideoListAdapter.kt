package com.learnwithsubs.feature_video_list.adapter

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.learnwithsubs.R
import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.models.VideoStatus


class VideoListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var videoList: ArrayList<Video> = ArrayList()
    private var videoSelected = ArrayList<Video>()
    private var isNormalMode = true
    private var onSelectChangeListener: OnSelectChange? = null

    fun setOnModeChangeListener(listener: OnSelectChange) {
        onSelectChangeListener = listener
    }

    fun updateData(newVideoList: ArrayList<Video>) {
        val selected = updateSelected(newVideoList)
        val diffResult = DiffUtil.calculateDiff(VideoDiffCallback(videoList, selected))
        videoList = ArrayList(selected)
        diffResult.dispatchUpdatesTo(this@VideoListAdapter)
    }

    private fun updateSelected(list: List<Video>): List<Video> {
        list.forEach {  video->
            val found = videoSelected.find { it.id == video.id }
            if (found != null) video.isSelected = found.isSelected
        }
        videoSelected.clear()
        videoSelected.addAll(list.filter { it.isSelected })
        return list
    }

    fun updateVideo(videoToUpdate: Video) {
        val position = videoList.indexOfFirst { it.id == videoToUpdate.id }
        if (position != -1) {
            val video = videoSelected.any { it.id == videoToUpdate.id }
            videoToUpdate.isSelected = video
            videoList[position] = videoToUpdate
            notifyItemChanged(position)
        }
    }

    fun updateSelection(position: Int, isSelected: Boolean) {
        videoList.getOrNull(position)?.let { video ->
            video.isSelected = isSelected
            if (video.isSelected)
                videoSelected.add(video)
            else
                videoSelected.remove(videoSelected.find { it.id == video.id })
        }
        onSelectChangeListener?.onSelectAll(isSelectAll = videoList.size == videoSelected.size)
        onSelectChangeListener?.onSingleSelected(isSingleSelected = videoSelected.size == 1)
    }

    fun selectAll() {
        videoList.forEach { it.isSelected = true }
        videoSelected = ArrayList(videoList.toList())
        changeMode(isNormalMode = false)
    }
    fun deselectAll() {
        videoList.forEach { it.isSelected = false }
        videoSelected.clear()
        notifyDataSetChanged()
    }
    fun clearSelection() {
        videoList.forEach { it.isSelected = false }
        videoSelected.clear()
        changeMode(isNormalMode = true)
    }
    fun changeMode(isNormalMode: Boolean) {
        this.isNormalMode = isNormalMode
        notifyDataSetChanged()
        onSelectChangeListener?.onModeChange(isNormalMode = isNormalMode)
        onSelectChangeListener?.onSelectAll(isSelectAll = videoList.size == videoSelected.size)
    }



    fun getVideoListSize(): Int {
        return videoList.size
    }
    fun getSelectedVideoSize(): Int {
        return videoSelected.size
    }
    fun getSelectedVideo(): List<Video> {
        return videoSelected
    }
    fun getEditableVideo(): Video? {
        return if (videoSelected.size == 1) videoSelected[0].copy()
        else null
    }
    fun getIsNormalMode(): Boolean {
        return isNormalMode
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VideoStatus.NORMAL_VIDEO.value -> NormalVideoViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.video_tile, parent, false), this@VideoListAdapter
            )

            VideoStatus.LOADING_VIDEO.value -> LoadingVideoViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.video_uploading_tile, parent, false), this@VideoListAdapter
            )

            else -> {
                LoadingVideoViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.video_uploading_tile, parent, false), this@VideoListAdapter
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val video = videoList[position]

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