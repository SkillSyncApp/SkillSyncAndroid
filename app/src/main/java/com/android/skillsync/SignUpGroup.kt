package com.android.skillsync

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.skillsync.databinding.ActivitySignUpGroupBinding
import com.android.skillsync.models.Group
import com.android.skillsync.models.Type
import com.android.skillsync.models.UserType
import com.android.skillsync.services.PlacesApiCall
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore

class SignUpGroup : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpGroupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var fieldErrorShown = false

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        setHintForEditText(R.id.email_group, null, R.string.team_email_title)
        setHintForEditText(R.id.password_group, null, R.string.password_title)
        setHintForEditText(R.id.team_name_group, null, R.string.team_name_title)
        setHintForEditText(R.id.institution_group, null, R.string.team_institution_title)
        setHintForEditText(R.id.team_description_group, null, R.string.team_description_title)
        setHintForEditText(R.id.members_group, null, R.string.team_members_name_title)

        binding.signUpBtn.setOnClickListener {
            val email = binding.emailGroup.editTextField.text.toString()
            val password = binding.passwordGroup.editTextField.text.toString()
            val groupName = binding.teamNameGroup.editTextField.text.toString()
            val institution = binding.institutionGroup.editTextField.text.toString()
            val teamDescription = binding.teamDescriptionGroup.editTextField.text.toString()
            val teamMembers = binding.membersGroup.editTextField.text.toString()

            if (isValidInputs(
                    email,
                    password,
                    groupName,
                    institution,
                    teamDescription,
                    teamMembers
                )
            ) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val groupId = it.result.user?.uid!!
                            createGroup(
                                groupId,
                                groupName,
                                institution,
                                teamDescription,
                                teamMembers
                            )
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            } else {
//                Toast.makeText(
//                    this,
//                    "Invalid input. Please check your entries.",
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        }
    }

    private fun isValidInputs(vararg inputs: String): Boolean {
        fieldErrorShown = false

        val validations = listOf(
            isValidEmail(inputs[0], "Your email"),
            isValidPassword(inputs[1], "Your password"),
            isValidString(inputs[2], "Your team name"),
            isValidString(inputs[3], "Your institution"),
            isValidStringDesc(inputs[4], "The summary about your group"),
            isValidString(inputs[5], "Your team member input")
        )

        return validations.all { it }
    }
    private fun isValidString(input: String, field: String): Boolean {
        val pattern = Regex("^[a-zA-Z,\\. ]+\$")
        val isLengthValid = input.length >= 4
        return (pattern.matches(input) && isLengthValid) || showError(field)
    }

    private fun isValidStringDesc(input: String, field: String): Boolean {
        val pattern = Regex("^[a-zA-Z,\\. ]+\$")
        return pattern.matches(input) || showError(field)
    }

    private fun showError(field: String): Boolean {
        if (!fieldErrorShown) {
            Toast.makeText(this, "$field is invalid. Please check your entry.", Toast.LENGTH_SHORT)
                .show()
            fieldErrorShown = true
        }
        return false
    }

    private fun isValidEmail(email: String, field: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || showError(field)
    }

    private fun isValidPassword(password: String, field: String): Boolean {
        return (password.length >= 6) || showError(field)
    }
    // remember user
    override fun onStart() {
        super.onStart()
        val user = firebaseAuth.currentUser

        if (user != null) {
            val intent = Intent(this, NewPostActivity::class.java)
            intent.putExtra("userEmail", user.email)
            startActivity(intent)
            finish()
        }
    }


    fun createGroup(
        groupId: String,
        groupName: String,
        institution: String,
        teamDescription: String,
        teamMembers: String
    ) {
        val database = Firebase.firestore
        val intent = Intent(this, SignIn::class.java)

        val groupEntity = Group(groupName, institution, teamDescription, teamMembers)

        val userType = UserType(Type.GROUP)
        database.collection("usersType").document(groupId).set(userType).addOnSuccessListener {
            val groupRef = database.collection("groups").document(groupId)
            groupRef.set(groupEntity).addOnSuccessListener {
                groupRef.addSnapshotListener { snapshot, e ->
                    logGroupNameFromDB(snapshot, e)
                    startActivity(intent)
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
        } else {
            Log.d("ERROR", "Group snapshot is null or does not exist.")
        }
    }

    private fun setHintForEditText(
        editTextGroupId: Int,
        hintResourceId: Int?,
        inputTitleResourceId: Int?
    ) {
        val editTextGroup = findViewById<View>(editTextGroupId)
        val editTextLabel = editTextGroup.findViewById<TextView>(R.id.edit_text_label)
        val editTextField = editTextGroup.findViewById<EditText>(R.id.edit_text_field)

        inputTitleResourceId?.let {
            editTextLabel.text = getString(it)
        }

        hintResourceId?.let {
            editTextField.hint = getString(it)
        }
    }
//    @RequiresApi(Build.VERSION_CODES.O_MR1)
//    fun searchPlaces(query: String) {
//        PlacesApiCall().getPlacesByQuery(this, query) { places ->
//            locationsAdapter.clear()
//            places?.forEach { locationsAdapter.add(it.title.plus(" | ").plus(it.address)) }
//            locationsAdapter.notifyDataSetChanged()
//        }
//    }

}
