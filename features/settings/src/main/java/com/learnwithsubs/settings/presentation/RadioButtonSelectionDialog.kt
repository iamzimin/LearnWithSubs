package com.learnwithsubs.settings.presentation

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.learnwithsubs.resource.R
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
        val radioButton = RadioButton(context)
        radioButton.text = text

        val typedArray = context.obtainStyledAttributes(intArrayOf(R.attr.text_color))
        val textColor = typedArray.getColor(0, Color.RED)
        typedArray.recycle()

        radioButton.setTextColor(textColor)
        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        val paddingStart = context.resources.getDimensionPixelSize(R.dimen.radio_button_text_start_padding)
        val verticalMargin = context.resources.getDimensionPixelSize(R.dimen.radio_button_text_vertical_padding)
        radioButton.setPaddingRelative(paddingStart, verticalMargin, 0, verticalMargin)
        radioButton.layoutParams = RadioGroup.LayoutParams(
            RadioGroup.LayoutParams.MATCH_PARENT,
            RadioGroup.LayoutParams.WRAP_CONTENT
        )
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