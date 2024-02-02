package com.android.skillsync

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.android.skillsync.ViewModel.PostViewModel
import com.android.skillsync.ViewModel.UserAuthViewModel
import com.android.skillsync.databinding.CustomInputFieldTextBinding
import com.android.skillsync.databinding.FragmentNewPostBinding
import com.android.skillsync.helpers.ImageHelper
import com.android.skillsync.helpers.ValidationHelper
import com.android.skillsync.models.Post.Post

class NewPostFragment : Fragment() {
    private lateinit var view: View
    private lateinit var postViewModel: PostViewModel
    private lateinit var post: Post
    private lateinit var imageView: ImageView
    private lateinit var imageHelper: ImageHelper

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

        imageHelper = ImageHelper(this, imageView)
        imageHelper.setImageViewClickListener()

        postViewModel = PostViewModel()

        setHintForEditText(
            editTextGroupId = R.id.post_title_group,
            hintResourceId = R.string.project_name_hint,
            inputTitleResourceId = R.string.project_name_title
        )
        setHintForEditText(
            editTextGroupId = R.id.post_description_group,
            hintResourceId = R.string.project_description_hint,
            inputTitleResourceId = R.string.project_description
        )
        setEventListeners()

        return view
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

    private fun setHintForEditText(editTextGroupId: Int, hintResourceId: Int? = null, inputTitleResourceId: Int? = null) {
        val editTextGroup = binding.root.findViewById<View>(editTextGroupId)
        val editTextLabel = editTextGroup?.findViewById<TextView>(R.id.edit_text_label)
        val editTextField = editTextGroup?.findViewById<EditText>(R.id.edit_text_field)

        inputTitleResourceId?.let {
            editTextLabel?.text = getString(it)
        }

        hintResourceId?.let {
            editTextField?.hint = getString(it)
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
