package com.android.skillsync

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.skillsync.databinding.FragmentSignUpGroupBinding
import com.android.skillsync.models.Group.FirebaseGroup
import com.android.skillsync.models.Type
import com.android.skillsync.models.UserType
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import androidx.core.content.ContextCompat
import com.android.skillsync.databinding.CustomInputFieldPasswordBinding
import com.android.skillsync.databinding.CustomInputFieldTextBinding

class SignUpGroupFragment : Fragment() {

    private var _binding: FragmentSignUpGroupBinding? = null
    private val binding get() = _binding!!

    private lateinit var view: View
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var signUpBtn: Button
    private var fieldErrorShown = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignUpGroupBinding.inflate(layoutInflater, container, false)
        view = binding.root

        firebaseAuth = FirebaseAuth.getInstance()

        // TODO REMOVE - JUST TO TEST SIGN IN PAGE
        val firebaseAuth = FirebaseAuth.getInstance()
        // Sign out the current user
        firebaseAuth.signOut()

        setHintForEditText(R.id.email_group, null, R.string.team_email_title)
        setHintForEditText(R.id.password_group, null, R.string.password_title)
        setHintForEditText(R.id.team_name_group, null, R.string.team_name_title)
        setHintForEditText(R.id.institution_group, null, R.string.team_institution_title)
        setHintForEditText(R.id.team_description_group, null, R.string.team_description_title)
        setHintForEditText(R.id.members_group, null, R.string.team_members_name_title)

        setEventListeners()

        return view
    }

    private fun setEventListeners() {
        signUpBtn = view.findViewById(R.id.sign_up_group_btn)

        signUpBtn.setOnClickListener {
            val email = binding.emailGroup//.editTextField.text.toString()
            val password = binding.passwordGroup//.editTextField.text.toString()
            val groupName = binding.teamNameGroup//.editTextField.text.toString()
            val institution = binding.institutionGroup//.editTextField.text.toString()
            val teamDescription = binding.teamDescriptionGroup//.editTextField.text.toString()
            val teamMembers = binding.membersGroup//.editTextField.text.toString()

            if (isValidInputs(
                    email,
                    password,
                    groupName,
                    institution,
                    teamDescription,
                    teamMembers
                )
            ) {
                firebaseAuth.createUserWithEmailAndPassword(
                    email.editTextField.text.toString(), password.editTextField.text.toString()
                )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val groupId = it.result.user?.uid!!
                            createGroup(
                                groupId,
                                email.editTextField.text.toString(),
                                groupName.editTextField.text.toString(),
                                institution.editTextField.text.toString(),
                                teamDescription.editTextField.text.toString(),
                                teamMembers.editTextField.text.toString()
                            )
                        } else {
                            Toast.makeText(
                                context, it.exception.toString(),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
            }
        }
    }

    private fun isValidInputs(
        email: CustomInputFieldTextBinding,
        password: CustomInputFieldPasswordBinding,
        teamName: CustomInputFieldTextBinding,
        institution: CustomInputFieldTextBinding,
        teamDescription: CustomInputFieldTextBinding,
        teamMembers: CustomInputFieldTextBinding,
    ): Boolean {
        fieldErrorShown = false

        val isStrValid: (String) -> Boolean = { input ->
            val pattern = Regex("^[a-zA-Z0-9,\\. ]+\$")
            val isLengthValid = input.length >= 4
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
            fieldValidation(teamName, isStrValid),
            fieldValidation(institution, isStrValid),
            fieldValidation(teamDescription, isStrValid),
            fieldValidation(teamMembers, isStrValid)
            /*TODO - do validations for password */
        )

        return validations.all { it }
    }

    private fun fieldValidation(
        inputGroup: CustomInputFieldTextBinding,
        validationCondition: (String) -> Boolean
    ): Boolean {
        val input = inputGroup.editTextField.text.toString()
        val isValid: Boolean

        if (input.isEmpty()) {
            showBlankError(inputGroup)
            isValid = false
        } else if (validationCondition(input)) {
            showValidInput(inputGroup)
            isValid = true
        } else {
            showTextError(inputGroup)
            isValid = false
        }
        return isValid
    }

    private fun passwordValidation(
        inputGroup: CustomInputFieldPasswordBinding,
        validationCondition: (String) -> Boolean
    ): Boolean {
        val input = inputGroup.editTextField.text.toString()
        val isValid: Boolean

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
        inputGroup.errorMessage?.visibility = View.INVISIBLE
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
        inputGroup.errorMessage?.visibility = View.INVISIBLE
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

    private fun showTextError(inputGroup: CustomInputFieldTextBinding) {
        if (inputGroup.editTextLabel.text.equals(R.string.team_description_title)) {
            inputGroup.errorMessage?.text = "Invalid group description"
        }
        if (inputGroup.editTextLabel.text.equals(R.string.team_members_name_title)) {
            inputGroup.errorMessage?.text = "Invalid group members name"
        }
        inputGroup.errorMessage?.text = "Invalid ${inputGroup.editTextLabel.text.toString()}"
        inputGroup.errorMessage?.visibility = View.VISIBLE
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

    // remember user
    override fun onStart() {
        super.onStart()
        val user = firebaseAuth.currentUser

        if (user != null) {
            val navController = Navigation.findNavController(requireView())
            navController.navigate(R.id.action_signInFragment_to_mapViewFragment)
        }
    }

    fun createGroup(
        groupId: String,
        groupEmail: String,
        groupName: String,
        institution: String,
        teamDescription: String,
        teamMembers: String
    ) {
        val database = Firebase.firestore

        val groupEntity = FirebaseGroup(
            groupEmail,
            groupName,
            institution,
            teamDescription,
            teamMembers.split(',')
        )

        val userType = UserType(Type.GROUP)
        database.collection("usersType").document(groupId).set(userType).addOnSuccessListener {
            val groupRef = database.collection("groups").document(groupId)
            groupRef.set(groupEntity).addOnSuccessListener {
                groupRef.addSnapshotListener { snapshot, e ->
                    logGroupNameFromDB(snapshot, e)
                    Navigation.findNavController(view)
                        .navigate(R.id.action_signUpGroupFragment_to_signInFragment)
                }
            }.addOnFailureListener {
                Log.d("ERROR", "fail to create group to app")
            }
        }
    }

    // TODO remove
    fun logGroupNameFromDB(snapshot: DocumentSnapshot?, e: FirebaseFirestoreException?) {
        if (e != null) Log.d("ERROR", "Listen failed.", e)

        if (snapshot != null && snapshot.exists()) {
            val groupName = snapshot.getString("name")
            Log.d("SUCCESS", "Group Name: $groupName")
        } else Log.d("ERROR", "Group snapshot is null or does not exist.")

    }

    private fun setHintForEditText(
        editTextGroupId: Int,
        hintResourceId: Int?, // TODO - FIELD here always get same value...
        inputTitleResourceId: Int?
    ) {
        val editTextGroup = view.findViewById<View>(editTextGroupId)
        val editTextLabel = editTextGroup?.findViewById<TextView>(R.id.edit_text_label)
        val editTextField = editTextGroup?.findViewById<EditText>(R.id.edit_text_field)

        inputTitleResourceId?.let {
            editTextLabel?.text = getString(it)
        }
    }
}
