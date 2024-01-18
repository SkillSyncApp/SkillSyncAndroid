package com.android.skillsync

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.skillsync.Navigations.navigate
import com.android.skillsync.ViewModel.StudentViewModel
import com.android.skillsync.databinding.CustomInputFieldPasswordBinding
import com.android.skillsync.databinding.CustomInputFieldTextBinding
import com.android.skillsync.databinding.FragmentSignUpStudentBinding
import com.android.skillsync.helpers.DialogHelper
import com.android.skillsync.helpers.DynamicTextHelper
import com.android.skillsync.helpers.ImageHelper
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

    private var fieldErrorShown = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignUpStudentBinding.inflate(layoutInflater, container, false)
        view = binding.root
        dynamicTextHelper = DynamicTextHelper(view)

        setHints()
        setEventListeners()

        return view
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

        val nameStudentGroup = binding.nameStudentGroup
        val emailGroup = binding.emailGroup
        val institutionGroup = binding.institutionGroup
        val bioGroup = binding.bioStudentGroup
        val passwordGroup = binding.passwordGroup
        val image =  imageHelper.getImageUrl() ?: "DEFAULT LOGO" // TODO
        studentViewModel = StudentViewModel()

        signUpBtn.setOnClickListener {
            if(isValidInputs(emailGroup, passwordGroup, nameStudentGroup, institutionGroup, bioGroup)) {
                val name = nameStudentGroup.editTextField.text.toString()
                val email = emailGroup.editTextField.text.toString()
                val bio = bioGroup.editTextField.text.toString()
                val password = passwordGroup.editTextField.text.toString()
                val institution = institutionGroup.editTextField.text.toString()

                student = Student(name = name, email = email, institution = institution, image = image, bio = bio)

                studentViewModel.createUserAsStudent(email, password, onSuccess, onError)
            }
        }
    }

    private val onError: (String?) -> Unit = {
        val dialogHelper = DialogHelper(requireContext(), it)
        dialogHelper.showErrorDialog()
    }

    private val onSuccess: () -> Unit = {
        studentViewModel.addStudent(student)
        view.navigate(R.id.action_signUpStudentFragment_to_signInFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    // TODO remove from fragment - validation helper

    private fun isValidInputs(
        email: CustomInputFieldTextBinding,
        password: CustomInputFieldPasswordBinding,
        name: CustomInputFieldTextBinding,
        institution: CustomInputFieldTextBinding,
        description: CustomInputFieldTextBinding, // TODO - description validation? IMO no need
    ): Boolean {
        fieldErrorShown = false

        val isStrValid: (String) -> Boolean = { input ->
            val pattern = Regex("^[a-zA-Z,\\. ]+\$")
            val isLengthValid = input.length >= 3
            pattern.matches(input) && isLengthValid
        }
        val isEmailValid: (String) -> Boolean = { email ->
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
        val isPassValid: (String) -> Boolean = { password ->
            password.length >= 6
            /*
                        val minLength = 8
                        val hasUpperCase = password.any { it.isUpperCase() }
                        val hasLowerCase = password.any { it.isLowerCase() }
                        val hasDigit = password.any { it.isDigit() }
                        val hasSpecialChar = password.any { it.isLetterOrDigit().not() }

                        password.length >= minLength &&
                                hasUpperCase &&
                                hasLowerCase &&
                                hasDigit &&
                                hasSpecialChar
             */
        }

        val validations = listOf(
            fieldValidation(email, isEmailValid),
            passwordValidation(password, isPassValid),
            fieldValidation(name, isStrValid),
            fieldValidation(institution, isStrValid),
            /*TODO - do validations for password */
        )

        return validations.all { it }
    }

    private fun fieldValidation(
        inputGroup: CustomInputFieldTextBinding,
        validationCondition: (String) -> Boolean
    ): Boolean {
        val input = inputGroup.editTextField.text.toString()

        val isValid: Boolean = if (input.isEmpty()) {
            showBlankError(inputGroup)
            false
        } else if (validationCondition(input)) {
            showValidInput(inputGroup)
            true
        } else {
            showTextError(inputGroup)
            false
        }
        return isValid
    }

    private fun passwordValidation(
        inputGroup: CustomInputFieldPasswordBinding,
        validationCondition: (String) -> Boolean
    ): Boolean {
        val input = inputGroup.editTextField.text.toString()
        val isValid: Boolean

        // TODO IF IT STAY LIKE THIS AND COMMENTS ARE REMOVED - NEED TO REFACTOR
        if (input.isEmpty()) {
            //showBlankError(inputGroup)
            isValid = false
        } else if (validationCondition(input)) {
            //  showValidInput(inputGroup)
            isValid = true
        } else {
            //  showTextError(inputGroup)
            isValid = false
        }
        return isValid
    }

    private fun showBlankError(inputGroup: CustomInputFieldTextBinding) {
        inputGroup.errorMessage.visibility = View.INVISIBLE
        inputGroup.editTextLine.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.red
            )
        )
        inputGroup.editTextLabel.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.red
            )
        )
    }

    private fun showValidInput(inputGroup: CustomInputFieldTextBinding) {
        inputGroup.errorMessage.visibility = View.INVISIBLE
        inputGroup.editTextLine.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.Light_Gray
            )
        )
        inputGroup.editTextLabel.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.Dark_Gray
            )
        )
    }

    @SuppressLint("SetTextI18n")
    private fun showTextError(inputGroup: CustomInputFieldTextBinding) {
        if (inputGroup.editTextLabel.text.equals(R.string.student_description_title)) {
            inputGroup.errorMessage.text = "Invalid group description"
        }
        if (inputGroup.editTextLabel.text.equals(R.string.team_members_name_title)) {
            inputGroup.errorMessage.text = "Invalid group members name"
        }
        inputGroup.errorMessage.text = "Invalid ${inputGroup.editTextLabel.text}"
        inputGroup.errorMessage.visibility = View.VISIBLE
        inputGroup.editTextLine.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.red
            )
        )
        inputGroup.editTextLabel.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.Dark_Gray
            )
        )
    }
}
