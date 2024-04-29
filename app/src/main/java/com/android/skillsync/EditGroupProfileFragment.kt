package com.android.skillsync

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.android.skillsync.ViewModel.StudentViewModel
import com.android.skillsync.ViewModel.UserAuthViewModel
import com.android.skillsync.databinding.CustomInputFieldTextBinding
import com.android.skillsync.databinding.FragmentEditGroupProfileBinding
import com.android.skillsync.helpers.ActionBarHelper
import com.android.skillsync.helpers.DynamicTextHelper
import com.android.skillsync.helpers.ValidationHelper
import com.android.skillsync.models.Student.Student

class EditGroupProfileFragment : Fragment() {
    private val userAuthViewModel: UserAuthViewModel by activityViewModels()
    private val studentViewModel: StudentViewModel by activityViewModels()

    private var _binding: FragmentEditGroupProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var view: View
    private lateinit var saveBtn: Button
    private lateinit var groupViewModel: StudentViewModel
    private lateinit var dynamicTextHelper: DynamicTextHelper
    private lateinit var emailAddress: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditGroupProfileBinding.inflate(layoutInflater, container, false)
        view = binding.root
        dynamicTextHelper = DynamicTextHelper(view)

        setHints()
        setUserData()
        setEventListeners()

        // Hide the BottomNavigationView
        ActionBarHelper.hideActionBarAndBottomNavigationView((requireActivity() as? AppCompatActivity))

        val backButton = view.findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_editGroupProfileFragment_to_ProfileFragment)
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setHints() {
        dynamicTextHelper.setTextViewText(R.id.group_institution, R.string.institution_title)
        dynamicTextHelper.setTextViewText(R.id.group_name, R.string.user_name_title)
        dynamicTextHelper.setTextViewText(R.id.group_bio, R.string.bio_title)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ActionBarHelper.showActionBarAndBottomNavigationView(requireActivity() as? AppCompatActivity)
        super.onCreate(savedInstanceState)
    }
    private fun setEventListeners() {
        saveBtn = view.findViewById(R.id.save_group_btn)

        groupViewModel = StudentViewModel()

        saveBtn.setOnClickListener {
            val groupName = binding.groupName
            val groupInstitution = binding.groupInstitution
            val groupBio = binding.groupBio

            if (isValidInputs(groupName, groupInstitution,groupBio)) {
                val name = groupName.editTextField.text.toString()
                val institution = groupInstitution.editTextField.text.toString()
                val bio = groupBio.editTextField.text.toString()

                val userId = userAuthViewModel.getUserId().toString()
                val updatedStudent = Student(
                    id = userId,
                    name = name,
                    institution = institution,
                    bio = bio,
                    email = emailAddress,
                    image = ""
                )

                groupViewModel.update(updatedStudent,updatedStudent.json,
                    onSuccessCallBack = {
                        Toast.makeText(context, "The data has been successfully updated", Toast.LENGTH_SHORT).show()
                        Log.d("EditProfilePage", "Success in update")
                    },
                    onFailureCallBack = {
                        Toast.makeText(context, "An error occurred while updating the data, please try again", Toast.LENGTH_SHORT).show()
                        Log.d("EditProfilePage", "Error in update")
                    }
                )
            }
        }
    }


    private fun isValidInputs(
        groupName: CustomInputFieldTextBinding,
        groupInstitution: CustomInputFieldTextBinding,
        groupBio: CustomInputFieldTextBinding
    ): Boolean {
        val validationResults = mutableListOf<Boolean>()
        validationResults.add(
            ValidationHelper.isValidString(groupName.editTextField.text.toString()).also { isValid ->
                ValidationHelper.handleValidationResult(isValid, groupName, requireContext())
            }
        )

        validationResults.add(
            ValidationHelper.isValidField(groupInstitution.editTextField.text.toString()).also { isValid ->
                ValidationHelper.handleValidationResult(isValid, groupInstitution, requireContext())
            }
        )
        validationResults.add(
            ValidationHelper.isValidField(groupBio.editTextField.text.toString()).also { isValid ->
                ValidationHelper.handleValidationResult(isValid, groupBio, requireContext())
            }
        )

        return validationResults.all { it }
    }

    private fun setUserData() {
        val userId = userAuthViewModel.getUserId().toString()
        studentViewModel.getStudent(userId) { student ->
            binding.groupName.editTextField.setText(student.name)
            binding.groupInstitution.editTextField.setText(student.institution)
            binding.groupBio.editTextField.setText(student.bio)
            emailAddress = student.email
        }
    }
}
