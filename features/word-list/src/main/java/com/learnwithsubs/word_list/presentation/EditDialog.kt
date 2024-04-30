package com.learnwithsubs.word_list.presentation

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import com.learnwithsubs.word_list.databinding.DialogWordListMenuEditBinding
import com.learnwithsubs.word_list.domain.models.WordTranslation
import com.learnwithsubs.word_list.presentation.adapter.WordListTitleAdapter
import java.util.Date

class EditDialog(fragment: Fragment, private val vm: WordListViewModel, private val adapter: WordListTitleAdapter) {
    val context = fragment.requireContext()
    private val editDialogBinding: DialogWordListMenuEditBinding = DialogWordListMenuEditBinding.inflate(fragment.layoutInflater)
    private val editMenu = Dialog(context)

    private val nativeLanguage: Pair<String, String> = vm.getNativeLanguage()
    private val learnLanguage: Pair<String, String> = vm.getLearningLanguage()

    init {
        editMenu.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogView = editDialogBinding.root
        val parentView = dialogView.parent as? ViewGroup
        parentView?.removeView(dialogView)
        editMenu.setContentView(dialogView)


        editDialogBinding.save.setOnClickListener {
            val word = vm.editableWord
            if (word == null) {
                val newWord = WordTranslation(
                    word = editDialogBinding.word.text.toString(),
                    translation = editDialogBinding.translation.text.toString(),
                    nativeLanguage = nativeLanguage.second,
                    learnLanguage = learnLanguage.second,
                    timestamp = Date().time
                )
                vm.editWord(newWord)
            } else {
                word.word = editDialogBinding.word.text.toString()
                word.translation = editDialogBinding.translation.text.toString()
                vm.editWord(word)
            }

            adapter.changeMode(isSelectionMode = false)
            editMenu.dismiss()
            vm.editableWord = null
        }

        editMenu.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        editMenu.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    fun openEditMenu() {
        editDialogBinding.word.setText(vm.editableWord?.word)
        editDialogBinding.translation.setText(vm.editableWord?.translation)

        editMenu.show()
    }

}
