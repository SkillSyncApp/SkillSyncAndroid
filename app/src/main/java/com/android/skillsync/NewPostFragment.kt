package com.android.skillsync

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

class NewPostFragment : Fragment() {
    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_new_post, container, false)
        setHintForEditText(
            editTextGroupId = R.id.project_name_group,
            hintResourceId = R.string.project_name_hint,
            inputTitleResourceId = R.string.project_name_title
        )
        setHintForEditText(
            editTextGroupId = R.id.project_description_group,
            hintResourceId = R.string.project_description_hint
        )
        setHintForEditText(
            editTextGroupId = R.id.project_deadline_group,
            inputTitleResourceId = R.string.project_deadline_title
        )
        setHintForEditText(
            editTextGroupId = R.id.project_availability_group,
            inputTitleResourceId = R.string.project__availability_title
        )

        return view
    }

    private fun setHintForEditText(editTextGroupId: Int, hintResourceId: Int? = null, inputTitleResourceId: Int? = null) {
        val editTextGroup = view.findViewById<View>(editTextGroupId)
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
