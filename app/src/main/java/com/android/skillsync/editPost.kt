package com.android.skillsync

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.skillsync.ViewModel.PostViewModel
import com.android.skillsync.databinding.FragmentEditPostBinding
import com.android.skillsync.helpers.DynamicTextHelper

class editPost : Fragment() {
    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var view: View
    private lateinit var dynamicTextHelper: DynamicTextHelper
    private lateinit var postViewModel: PostViewModel

    var name: TextView? = null
    var details: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditPostBinding.inflate(layoutInflater, container, false)
        view = binding.root
        dynamicTextHelper = DynamicTextHelper(view)

        postViewModel = PostViewModel()

        val args = arguments
        val postId = args?.getString("postId").toString()

        setPostData(postId)
        setHints()

        return view
    }

    private fun setHints() {
        dynamicTextHelper.setHintForEditText(R.id.project_name, R.string.project_name_hint, R.string.project_name_title)
        dynamicTextHelper.setHintForEditText(R.id.multi_line_project_description, R.string.project_description_hint, R.string.project_description)
    }

    private fun setPostData(postId: String) {
        Log.d("postId", postId)
        postViewModel.getPostById(postId)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}