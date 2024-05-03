package com.android.skillsync

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.skillsync.ViewModel.PostViewModel
import com.android.skillsync.ViewModel.UserAuthViewModel
import com.android.skillsync.databinding.FragmentEditPostBinding
import com.android.skillsync.helpers.DynamicTextHelper
import com.android.skillsync.helpers.ImageHelper
import com.android.skillsync.models.Post.Post
import com.squareup.picasso.Picasso
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class editPost : Fragment() {
    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var view: View
    private lateinit var dynamicTextHelper: DynamicTextHelper
    private lateinit var postViewModel: PostViewModel
    private lateinit var imageHelper: ImageHelper
    private lateinit var imageView: ImageView

    private val userAuthViewModel: UserAuthViewModel by activityViewModels()
    var titleConstraintLayout: ConstraintLayout? = null
    var detailsConstraintlayout: ConstraintLayout? = null

    var postId = ""

    var title: TextView? = null
    var details: TextView? = null
    var updatePost: Button? = null
    var imagePost = ""

    @RequiresApi(Build.VERSION_CODES.O)
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

        val backButton = view.findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        title = titleConstraintLayout?.findViewById(R.id.edit_text_field)
        details = detailsConstraintlayout?.findViewById(R.id.edit_text_field)

        addEventListeners()
        setPostData(postId)
        setHints()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addEventListeners() {

        updatePost?.setOnClickListener {
            val updatedPost =  Post(
            ownerId = userAuthViewModel.getUserId().toString(),
            title = title?.text.toString(),
            content = details?.text.toString(),
                imagePath = if (imageHelper.isImageSelected()) {
                    imageHelper.getImageUrl() ?: ""
                } else {
                    imagePost
                }
        )
            updatedPost.id = postId
            lifecycleScope.launch {
                val result = postViewModel.update(postId, updatedPost.json)
                if (result) {
                    delay(2000)
                    Toast.makeText(requireContext(), "Post updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to update post", Toast.LENGTH_SHORT).show()
                }
            }
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
            if(postData?.imagePath != "") {
                if (postData != null) {
                    imagePost = postData.imagePath
                }
                Picasso.get().load(postData?.imagePath).into(imageView)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}