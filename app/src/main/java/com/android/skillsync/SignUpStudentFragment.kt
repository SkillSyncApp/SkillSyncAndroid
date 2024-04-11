package com.android.skillsync

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpStudentBinding.inflate(layoutInflater, container, false)
        view = binding.root
        dynamicTextHelper = DynamicTextHelper(view)

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
            findNavController().navigateUp()        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setHints() {
        dynamicTextHelper.setTextViewText(R.id.email_group, R.string.user_email_title)
        dynamicTextHelper.setTextViewText(R.id.password_group, R.string.password_title)
        dynamicTextHelper.setTextViewText(R.id.institution_group, R.string.team_institution_title)
        dynamicTextHelper.setTextViewText(R.id.name_student_group, R.string.name)
        dynamicTextHelper.setTextViewText(R.id.bio_student_group, R.string.bio_title)
    }

    private fun setEventListeners() {
        signUpBtn = view.findViewById(R.id.sign_up_student_btn)
        imageView = view.findViewById(R.id.studentImage)

        imageHelper = ImageHelper(this, imageView)
        imageHelper.setImageViewClickListener()

        val image = imageHelper.getImageUrl() ?: "DEFAULT LOGO" // TODO
        studentViewModel = StudentViewModel()

        signUpBtn.setOnClickListener {
            if (isValidInputs(
                    binding.emailGroup,
                    binding.passwordGroup,
                    binding.nameStudentGroup,
                    binding.institutionGroup,
                    binding.bioStudentGroup
                )
            ) {
                val name = binding.nameStudentGroup.editTextField.text.toString()
                val email = binding.emailGroup.editTextField.text.toString()
                val bio = binding.bioStudentGroup.editTextField.text.toString()
                val password = binding.passwordGroup.editTextField.text.toString()
                val institution = binding.institutionGroup.editTextField.text.toString()

                student = Student(
                    name = name,
                    email = email,
                    institution = institution,
                    image = image,
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

    private val onSuccess: () -> Unit = {
        studentViewModel.addStudent(student)
        view.navigate(R.id.action_signUpStudentFragment_to_signInFragment)
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
            ValidationHelper.isValidString(institution.editTextField.text.toString())
                .also { isValid ->
                    ValidationHelper.handleValidationResult(isValid, institution, requireContext())
                }
        )

        validationResults.add(
            ValidationHelper.isValidString(bio.editTextField.text.toString()).also { isValid ->
                ValidationHelper.handleValidationResult(isValid, bio, requireContext())
            }
        )

        return validationResults.all { it }
    }
}
