package com.android.skillsync

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.skillsync.ViewModel.PostViewModel
import com.android.skillsync.databinding.FragmentEditPostBinding
import com.android.skillsync.helpers.DynamicTextHelper
import com.squareup.picasso.Picasso

class editPost : Fragment() {
    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var view: View
    private lateinit var dynamicTextHelper: DynamicTextHelper
    private lateinit var postViewModel: PostViewModel

    var name: ConstraintLayout? = null
    var details: ConstraintLayout? = null
    var imageView: ImageView? = null

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

        name = view.findViewById(R.id.edit_project_name)
        details = view.findViewById(R.id.edit_multi_line_project_description)
        imageView = view.findViewById(R.id.edit_image_to_upload)

        setPostData(postId)
        setHints()

        return view
    }

    private fun setHints() {
        dynamicTextHelper.setHintForEditText(R.id.edit_project_name, R.string.project_name_hint, R.string.project_name_title)
        dynamicTextHelper.setHintForEditText(R.id.edit_multi_line_project_description, R.string.project_description_hint, R.string.project_description)
    }

    private fun setPostData(postId: String) {
        postViewModel.getPostById(postId) { postData ->
            name?.findViewById<TextView>(R.id.edit_text_field)?.text = postData?.title
            details?.findViewById<TextView>(R.id.edit_text_field)?.text = postData?.content
            if(postData?.imagePath != "")
                Picasso.get().load(postData?.imagePath).into(imageView)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}