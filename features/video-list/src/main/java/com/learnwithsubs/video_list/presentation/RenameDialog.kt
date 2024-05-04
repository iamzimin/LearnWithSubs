package com.learnwithsubs.video_list.presentation

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.learnwithsubs.resource.R
import com.learnwithsubs.video_list.databinding.DialogVideoListMenuRenameBinding
import com.learnwithsubs.video_list.domain.models.VideoLoadingType
import com.learnwithsubs.video_list.presentation.adapter.VideoListAdapter
import com.learnwithsubs.video_list.presentation.videos.VideoListViewModel

class RenameDialog(fragment: Fragment, private val vm: VideoListViewModel, private val adapter: VideoListAdapter) {
    val context = fragment.requireContext()
    private val renameDialogBinding: DialogVideoListMenuRenameBinding = DialogVideoListMenuRenameBinding.inflate(fragment.layoutInflater)
    private val renameMenu = Dialog(context)

    init {
        renameMenu.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogView = renameDialogBinding.root
        val parentView = dialogView.parent as? ViewGroup
        parentView?.removeView(dialogView)
        renameMenu.setContentView(dialogView)

        renameDialogBinding.videoNameEdittext.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                save(textView)
                true
            } else false
        }
        renameDialogBinding.save.setOnClickListener {
            save(renameDialogBinding.videoNameEdittext)
        }

        renameMenu.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        renameMenu.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun openRenameMenu() {
        renameDialogBinding.videoNameEdittext.setText(vm.editableVideo?.name)

        renameMenu.show()
    }

    private fun save(textView: TextView) {
        textView.clearFocus()
        val video = vm.videoList.value?.find { it.id == vm.editableVideo?.id }?.copy()

        if (video == null) {
            // Если видео не найдено - его нельзя редактировать
            Toast.makeText(context, context.getString(R.string.the_video_does_not_exist), Toast.LENGTH_SHORT).show()
        } else if (video.loadingType != VideoLoadingType.DONE) {
            // Если видео не загружено - его нельзя редактировать
            Toast.makeText(context, context.getString(R.string.video_is_uploading), Toast.LENGTH_SHORT).show()
        } else {
            // Обновляем и загружаем видео
            video.name = textView.text.toString()
            vm.editVideo(video = video)
        }

        adapter.changeMode(isSelectionMode = false)
        renameMenu.dismiss()
        vm.editableVideo = null
    }

}

