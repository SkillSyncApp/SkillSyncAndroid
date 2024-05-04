package com.android.skillsync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.skillsync.Navigations.navigate
import com.android.skillsync.ViewModel.StudentViewModel
import com.android.skillsync.databinding.CustomInputFieldPasswordBinding
import com.android.skillsync.databinding.CustomInputFieldTextBinding
import com.android.skillsync.databinding.FragmentSignUpStudentBinding
import com.android.skillsync.helpers.ActionBarHelper
import com.android.skillsync.helpers.DialogHelper
import com.android.skillsync.helpers.DynamicTextHelper
import com.android.skillsync.helpers.ImageHelper
import com.android.skillsync.helpers.ImageUploadListener
import com.android.skillsync.helpers.ValidationHelper
import com.android.skillsync.models.Student.Student

class SignUpStudentFragment : Fragment() {

    private var _binding: FragmentSignUpStudentBinding? = null
    private val binding get() = _binding!!

    private lateinit var view: View
    private lateinit var signUpBtn: Button
    private lateinit var imageHelper: ImageHelper
    private lateinit var imageView: ImageView
    private lateinit var student: Student
    private lateinit var studentViewModel: StudentViewModel
    private lateinit var dynamicTextHelper: DynamicTextHelper
    private  lateinit var loadingOverlay: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpStudentBinding.inflate(layoutInflater, container, false)
        view = binding.root
        dynamicTextHelper = DynamicTextHelper(view)

        loadingOverlay = view.findViewById(R.id.signup_student_loading_overlay);
        loadingOverlay?.visibility = View.INVISIBLE

        setHints()
        setEventListeners()

        // Hide the BottomNavigationView
        ActionBarHelper.hideActionBarAndBottomNavigationView((requireActivity() as? AppCompatActivity))

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backButton = view.findViewById<ImageView>(R.id.back_button)


        backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setHints() {
        dynamicTextHelper.setTextViewText(R.id.email_student, R.string.user_email_title)
        dynamicTextHelper.setTextViewText(R.id.password_student, R.string.password)
        dynamicTextHelper.setTextViewText(R.id.institution, R.string.institution_title)
        dynamicTextHelper.setTextViewText(R.id.name_student, R.string.user_name_title)
        dynamicTextHelper.setTextViewText(R.id.bio_student, R.string.bio_title)
    }

    private fun setEventListeners() {
        signUpBtn = view.findViewById(R.id.sign_up_student_btn)
        imageView = view.findViewById(R.id.studentImage)

        imageHelper = ImageHelper(this, imageView, object : ImageUploadListener {
            override fun onImageUploaded(imageUrl: String) {
                // Perform actions after image upload completes
                loadingOverlay?.visibility = View.INVISIBLE
            }
        })

        imageHelper.setImageViewClickListener {
            loadingOverlay?.visibility = View.VISIBLE
        }

        studentViewModel = StudentViewModel()

        signUpBtn.setOnClickListener {
            val email = binding.emailStudent
            val password = binding.passwordStudent
            val name = binding.nameStudent
            val institution = binding.institution
            val bio = binding.bioStudent
            val logo = imageHelper.getImageUrl() ?:
            "https://firebasestorage.googleapis.com/v0/b/skills-e4dc8.appspot.com/o/images%2FuserAvater.png?alt=media&token=1fa189ff-b5df-4b1a-8673-2f8e11638acc"

            if (isValidInputs(
                    email,
                    password,
                    name,
                    institution,
                    bio
                )
            ) {
                val name = name.editTextField.text.toString()
                val email = email.editTextField.text.toString()
                val bio = bio.editTextField.text.toString()
                val password = password.editTextField.text.toString()
                val institution = institution.editTextField.text.toString()

                student = Student(
                    name = name,
                    email = email,
                    institution = institution,
                    image = logo,
                    bio = bio
                )

                studentViewModel.createUserAsStudent(email, password, onSuccess, onError)
            }
        }

    }

    private val onError: (String?) -> Unit = {
        val dialogHelper = DialogHelper("Sorry,", requireContext(), it)
        dialogHelper.showDialogMessage()
    }

    private val onSuccess: (String?) -> Unit = { userId ->
        userId?.let { id ->
            studentViewModel.addStudent(student.copy(id = id))
            Toast.makeText(requireContext(), "Account created successfully", Toast.LENGTH_SHORT).show()
            view.navigate(R.id.action_signUpStudentFragment_to_signInFragment)
        }
    }

    private fun isValidInputs(
        email: CustomInputFieldTextBinding,
        password: CustomInputFieldPasswordBinding,
        name: CustomInputFieldTextBinding,
        institution: CustomInputFieldTextBinding,
        bio: CustomInputFieldTextBinding
    ): Boolean {
        val validationResults = mutableListOf<Boolean>()
        validationResults.add(
            ValidationHelper.isValidEmail(email.editTextField.text.toString()).also { isValid ->
                ValidationHelper.handleValidationResult(isValid, email, requireContext())
            }
        )

        validationResults.add(
            ValidationHelper.isValidPassword(password.editTextField.text.toString())
                .also { isValid ->
                    ValidationHelper.handleValidationResult(isValid, password, requireContext())
                }
        )

        validationResults.add(
            ValidationHelper.isValidString(name.editTextField.text.toString()).also { isValid ->
                ValidationHelper.handleValidationResult(isValid, name, requireContext())
            }
        )

        validationResults.add(
            ValidationHelper.isValidField(institution.editTextField.text.toString())
                .also { isValid ->
                    ValidationHelper.handleValidationResult(isValid, institution, requireContext())
                }
        )

        validationResults.add(
            ValidationHelper.isValidField(bio.editTextField.text.toString()).also { isValid ->
                ValidationHelper.handleValidationResult(isValid, bio, requireContext())
            }
        )

        return validationResults.all { it }
    }
}
