package com.android.skillsync

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.skillsync.databinding.FragmentEditPostBinding
import com.android.skillsync.helpers.DynamicTextHelper

class editPost : Fragment() {
    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var view: View
    private lateinit var dynamicTextHelper: DynamicTextHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditPostBinding.inflate(layoutInflater, container, false)
        view = binding.root
        dynamicTextHelper = DynamicTextHelper(view)

        setHints()

        return view
    }

    private fun setHints() {
        dynamicTextHelper.setHintForEditText(R.id.project_name, R.string.project_name_hint, R.string.project_name_title)
        dynamicTextHelper.setHintForEditText(R.id.multi_line_project_description, R.string.project_description_hint, R.string.project_description)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}