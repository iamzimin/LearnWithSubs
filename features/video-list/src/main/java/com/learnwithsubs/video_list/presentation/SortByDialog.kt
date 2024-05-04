package com.learnwithsubs.video_list.presentation

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import com.example.base.util.OrderType
import com.example.base.util.VideoOrder
import com.learnwithsubs.resource.R
import com.learnwithsubs.video_list.databinding.DialogVideoListMenuSortByBinding
import com.learnwithsubs.video_list.presentation.adapter.VideoListAdapter
import com.learnwithsubs.video_list.presentation.videos.VideoListViewModel

class SortByDialog(fragment: Fragment, private val vm: VideoListViewModel, private val adapter: VideoListAdapter) {
    val context = fragment.requireContext()
    private val sortByDialogBinding: DialogVideoListMenuSortByBinding =  DialogVideoListMenuSortByBinding.inflate(fragment.layoutInflater)
    private val sortByDialog = Dialog(context)

    init {
        sortByDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogView = sortByDialogBinding.root
        val parentView = dialogView.parent as? ViewGroup
        parentView?.removeView(dialogView)
        sortByDialog.setContentView(dialogView)

        sortByDialogBinding.nameCheckBox.isChecked = vm.getVideoOrder() is VideoOrder.Name
        sortByDialogBinding.dateCheckBox.isChecked = vm.getVideoOrder() is VideoOrder.Date
        sortByDialogBinding.durationCheckBox.isChecked = vm.getVideoOrder() is VideoOrder.Duration

        val sortType = vm.getVideoOrder()
        setButtonColors(ascending = sortType.orderType is OrderType.Ascending)

        sortByDialogBinding.ascending.setOnClickListener {
            setButtonColors(ascending = true)
            vm.setOrderType(newOrderType = OrderType.Ascending)
            val currentVideoOrder = vm.getVideoOrder()
            vm.setVideoOrder(currentVideoOrder)
        }

        sortByDialogBinding.descending.setOnClickListener {
            setButtonColors(ascending = false)
            vm.setOrderType(newOrderType = OrderType.Descending)
            val currentVideoOrder = vm.getVideoOrder()
            vm.setVideoOrder(currentVideoOrder)
        }

        sortByDialogBinding.cardViewName.setOnClickListener {
            val currentOrderType = vm.getOrderType()
            vm.setVideoOrder(VideoOrder.Name(currentOrderType))
        }
        sortByDialogBinding.cardViewDate.setOnClickListener {
            val currentOrderType = vm.getOrderType()
            vm.setVideoOrder(VideoOrder.Date(currentOrderType))
        }
        sortByDialogBinding.cardViewDuration.setOnClickListener {
            val currentOrderType = vm.getOrderType()
            vm.setVideoOrder(VideoOrder.Duration(currentOrderType))
        }

        sortByDialogBinding.clearButton.setOnClickListener {
            vm.setVideoOrder(VideoListViewModel.DEFAULT_SORT_MODE)
        }
        sortByDialogBinding.applyButton.setOnClickListener {
            sortByDialog.dismiss()
            val data = vm.videoList.value ?: return@setOnClickListener
            adapter.updateData(ArrayList(vm.getSortedVideoList(data)))
        }

        vm.videoOrder.observe(fragment.viewLifecycleOwner) { sortMode ->
            sortByDialogBinding.nameCheckBox.isChecked = sortMode is VideoOrder.Name
            sortByDialogBinding.dateCheckBox.isChecked = sortMode is VideoOrder.Date
            sortByDialogBinding.durationCheckBox.isChecked = sortMode is VideoOrder.Duration
        }

        sortByDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        sortByDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        sortByDialog.window?.setGravity(Gravity.BOTTOM)
    }

    fun openSortByMenu() {
        sortByDialog.show()
    }

    private fun setButtonColors(ascending: Boolean) {
        sortByDialogBinding.ascending.setBackgroundColor(
            if (ascending) resolveColorAttr(R.attr.button_pressed)
            else resolveColorAttr(R.attr.button_normal))
        sortByDialogBinding.descending.setBackgroundColor(
            if (ascending) resolveColorAttr(R.attr.button_normal)
            else resolveColorAttr(R.attr.button_pressed))
    }

    private fun resolveColorAttr(attr: Int): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }
}