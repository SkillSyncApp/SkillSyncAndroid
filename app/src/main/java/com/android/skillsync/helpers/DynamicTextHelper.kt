package com.android.skillsync.helpers

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.skillsync.R

class DynamicTextHelper(private val view: View) {
    fun setTextViewText(parentId: Int, stringResourceId: Int): TextView {
        val parentGroup = view.findViewById<ConstraintLayout>(parentId)
        val textView = parentGroup.findViewById<TextView>(R.id.edit_text_label)
        textView.text = view.context.getString(stringResourceId)
        return textView
    }
}
