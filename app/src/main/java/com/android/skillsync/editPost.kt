package com.android.skillsync

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import com.android.skillsync.ViewModel.PostViewModel
import com.android.skillsync.ViewModel.UserAuthViewModel
import com.android.skillsync.databinding.FragmentEditPostBinding
import com.android.skillsync.helpers.DynamicTextHelper
import com.android.skillsync.helpers.ImageHelper
import com.android.skillsync.models.Post.Post
import com.squareup.picasso.Picasso

class editPost : Fragment() {
    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var view: View
    private lateinit var dynamicTextHelper: DynamicTextHelper
    private lateinit var postViewModel: PostViewModel
    private val userAuthViewModel: UserAuthViewModel by activityViewModels()
    private lateinit var imageHelper: ImageHelper

    var titleConstraintLayout: ConstraintLayout? = null
    var detailsConstraintlayout: ConstraintLayout? = null
    private lateinit var imageView: ImageView
    var postId = ""

    var title: TextView? = null
    var details: TextView? = null

    var updatePost: Button? = null


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
        postId = args?.getString("postId").toString()

        titleConstraintLayout = view.findViewById(R.id.edit_project_name)
        detailsConstraintlayout = view.findViewById(R.id.edit_multi_line_project_description)
        imageView = view.findViewById(R.id.edit_image_to_upload)
        updatePost = view.findViewById(R.id.save_post)

        imageHelper = ImageHelper(this, imageView)
        imageHelper.setImageViewClickListener()

        title = titleConstraintLayout?.findViewById(R.id.edit_text_field)
        details = detailsConstraintlayout?.findViewById(R.id.edit_text_field)

        addEventListeners()
        setPostData(postId)
        setHints()

        return view
    }

    private fun addEventListeners() {

        updatePost?.setOnClickListener {
            val updatedPost =  Post(
            ownerId = userAuthViewModel.getUserId().toString(),
            title = title?.text.toString(),
            content = details?.text.toString(),
            imagePath = imageHelper.getImageUrl() ?: ""
        )
            updatedPost.id = postId
            postViewModel.update(postId, updatedPost.json)
        }
    }

    private fun setHints() {
        dynamicTextHelper.setHintForEditText(R.id.edit_project_name, R.string.project_name_hint, R.string.project_name_title)
        dynamicTextHelper.setHintForEditText(R.id.edit_multi_line_project_description, R.string.project_description_hint, R.string.project_description)
    }

    private fun setPostData(postId: String) {
        postViewModel.getPostById(postId) { postData ->
            title?.text = postData?.title
            details?.text = postData?.content
            if(postData?.imagePath != "") Picasso.get().load(postData?.imagePath).into(imageView)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}