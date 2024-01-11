package com.android.skillsync

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

class NewPostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHintForEditText(R.id.project_name_group, R.string.project_name_hint, R.string.project_name_title)
        setHintForEditText(R.id.project_description_group,  R.string.project_description_hint,null)
        setHintForEditText(R.id.project_deadline_group,null, R.string.project_deadline_title)
        setHintForEditText(R.id.project_availability_group,null, R.string.project__availability_title)

        return inflater.inflate(R.layout.fragment_new_post, container, false)
    }

    private fun setHintForEditText(editTextGroupId: Int, hintResourceId: Int?, inputTitleResourceId: Int?) {
        val editTextGroup = view?.findViewById<View>(editTextGroupId)
        val editTextLabel = editTextGroup?.findViewById<TextView>(R.id.edit_text_label)
        val editTextField = editTextGroup?.findViewById<EditText>(R.id.edit_text_field)

        inputTitleResourceId?.let {
            editTextLabel?.text = getString(it)
        }

        hintResourceId?.let {
            editTextField?.hint = getString(it)
        }
    }
}
