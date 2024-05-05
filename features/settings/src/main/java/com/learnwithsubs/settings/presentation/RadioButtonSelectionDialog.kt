package com.learnwithsubs.settings.presentation

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.example.settings.R
import com.example.settings.databinding.SettingsRadioButtonDialogBinding

class RadioButtonSelectionDialog(
    fragment: Fragment,
    private val title: String,
    private val options: Array<String>,
    private val selected: String,
) {
    private val context: Context = fragment.requireContext()
    private val settingsRadioButtonDialogBinding: SettingsRadioButtonDialogBinding = SettingsRadioButtonDialogBinding.inflate(fragment.layoutInflater)
    private val radioButtonDialog: Dialog = Dialog(context)
    private var radioGroup: RadioGroup

    private lateinit var onItemSelectedListener: ((String) -> Unit)
    fun setOnItemSelectedListener(listener: (String) -> Unit) {
        this.onItemSelectedListener = listener
    }
    private fun notifyItemSelectedListener(selectedText: String) {
        onItemSelectedListener.invoke(selectedText)
    }

    init {
        radioButtonDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogView = settingsRadioButtonDialogBinding.root
        val parentView = dialogView.parent as? ViewGroup
        parentView?.removeView(dialogView)
        radioButtonDialog.setContentView(dialogView)

        radioButtonDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        radioButtonDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        radioButtonDialog.window?.setGravity(Gravity.CENTER)

        settingsRadioButtonDialogBinding.title.text = title
        radioGroup = settingsRadioButtonDialogBinding.radioGroup

        for (option in options) {
            addRadioButton(option)
        }
    }

    private fun addRadioButton(text: String) {
        val inflater = LayoutInflater.from(context)
        val radioButton = inflater.inflate(R.layout.radio_button, null) as RadioButton

        radioButton.text = text
        radioButton.tag = text
        radioGroup.addView(radioButton)

        if (text == selected) {
            radioButton.isChecked = true
        }
        radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val selectedText = buttonView.text.toString()
                notifyItemSelectedListener(selectedText)
                radioButtonDialog.dismiss()
            }
        }
    }


    fun openMenu() {
        radioButtonDialog.show()
    }
}