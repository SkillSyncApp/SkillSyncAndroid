package com.android.skillsync

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import com.android.skillsync.ViewModel.PostViewModel
import com.android.skillsync.ViewModel.UserAuthViewModel
import com.android.skillsync.databinding.CustomInputFieldTextBinding
import com.android.skillsync.databinding.FragmentNewPostBinding
import com.android.skillsync.helpers.DynamicTextHelper
import com.android.skillsync.helpers.ImageHelper
import com.android.skillsync.helpers.ValidationHelper
import com.android.skillsync.models.Post.Post

class NewPostFragment : Fragment() {
    private lateinit var view: View
    private lateinit var postViewModel: PostViewModel
    private lateinit var post: Post
    private lateinit var imageView: ImageView
    private lateinit var imageHelper: ImageHelper
    private lateinit var dynamicTextHelper: DynamicTextHelper

    private var _binding: FragmentNewPostBinding? = null
    private val binding get() = _binding!!
    private val userAuthViewModel: UserAuthViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPostBinding.inflate(layoutInflater, container, false)
        view = binding.root
        imageView = binding.imageToUpload
        dynamicTextHelper = DynamicTextHelper(view)

        imageHelper = ImageHelper(this, imageView)
        imageHelper.setImageViewClickListener()

        postViewModel = PostViewModel()

        setHints()
        setEventListeners()

        return view
    }

    private fun setHints() {
        dynamicTextHelper.setHintForEditText(R.id.post_title_group, R.string.project_name_hint, R.string.project_name_title)
        dynamicTextHelper.setHintForEditText(R.id.post_description_group, R.string.project_name_hint, R.string.project_description)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setEventListeners() {
       binding.postButton.setOnClickListener{
            val titleGroup =  binding.postTitleGroup
            val contentGroup = binding.postDescriptionGroup
            val image = imageHelper.getImageUrl() ?: ""

            if (isValidInputs(titleGroup, contentGroup)) {
                val title = titleGroup.editTextField.text.toString()
                val content = contentGroup.editTextField.text.toString()

                post = Post(
                    ownerId = userAuthViewModel.getUserId().toString(),
                    title = title,
                    content = content,
                    imagePath = image
                )
                postViewModel.addPost(post)
            }
        }
    }

    private fun isValidInputs(
        title: CustomInputFieldTextBinding,
        content: CustomInputFieldTextBinding,
    ): Boolean {
        val validationResults = mutableListOf<Boolean>()
        validationResults.add(
            ValidationHelper.isValidString(title.editTextField.text.toString()).also { isValid ->
                ValidationHelper.handleValidationResult(isValid, title, requireContext())
            }
        )

        validationResults.add(
            ValidationHelper.isValidString(content.editTextField.text.toString()).also { isValid ->
                ValidationHelper.handleValidationResult(isValid, content, requireContext())
            }
        )

        return validationResults.all { it }
    }
}
