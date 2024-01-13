package com.android.skillsync

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.skillsync.databinding.CustomInputFieldPasswordBinding
import com.android.skillsync.databinding.CustomInputFieldTextBinding
import com.android.skillsync.databinding.FragmentSignUpCompanyBinding
import com.android.skillsync.models.Comapny.FirebaseCompany
import com.android.skillsync.models.CompanyLocation
import com.android.skillsync.models.Type
import com.android.skillsync.models.UserType
import com.android.skillsync.models.serper.Place
import com.android.skillsync.services.PlacesApiCall
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.firestore

class SignUpCompanyFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var locationsAdapter: ArrayAdapter<String>
    private lateinit var placesSuggestions: Array<Place>

    private lateinit var companyLocation: CompanyLocation;
    private var fieldErrorShown = false

    private var _binding: FragmentSignUpCompanyBinding? = null
    private val binding get() = _binding!!

    private lateinit var view: View
    private lateinit var signUpCompany: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignUpCompanyBinding.inflate(layoutInflater, container, false)
        view = binding.root

        firebaseAuth = FirebaseAuth.getInstance()

        // TODO REMOVE - JUST TO TEST SIGN IN PAGE
        val firebaseAuth = FirebaseAuth.getInstance()
        // Sign out the current user
        firebaseAuth.signOut()

        initLocationsAutoComplete()

        setHints()
        setEventListeners()

        return view
    }

    // remember user
    override fun onStart() {
        super.onStart()
        val user = firebaseAuth.currentUser

        if (user != null) {
            // you can pass arguments by define in nav graph
            //<argument
            //android:name="myArg"
            //app:argType="integer"
            //android:defaultValue="1" />
            // ActionStartFragmentToSecondFragment action =
            //StartFragmentDirections.actionStartFragmentToSecondFragment("123456");
            //btn.setOnClickListener(Navigation.createNavigateOnClickListener(action));

            // TODO REMOVE - JUST TO TEST SIGN IN PAGE
//            val firebaseAuth = FirebaseAuth.getInstance()
            // Sign out the current user
//            firebaseAuth.signOut()

            Navigation.findNavController(binding.root)
                .navigate(R.id.action_signUpCompanyFragment_to_mapViewFragment)
        }
    }

    private fun initLocationsAutoComplete() {
        val autoCompany: AutoCompleteTextView? = view.findViewById(R.id.companySuggestion)

        locationsAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            ArrayList()
        )

        autoCompany?.setAdapter(locationsAdapter)
        autoCompany?.setOnItemClickListener { adapterView, view, i, l ->
            val selectedPlace = placesSuggestions[i];
            companyLocation = CompanyLocation(
                selectedPlace.address,
                GeoPoint(selectedPlace.latitude.toDouble(), selectedPlace.longitude.toDouble())
            );
        }

        autoCompany?.addTextChangedListener(object : TextWatcher {
            @RequiresApi(Build.VERSION_CODES.O_MR1)
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.isNotEmpty()) searchPlaces(s.toString())
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setEventListeners() {
        signUpCompany = view.findViewById(R.id.sign_up_company_btn)

        signUpCompany.setOnClickListener {
            val companyName = binding.companyNameGroup//.editTextField.text.toString()
            val email = binding.emailGroup//.editTextField.text.toString()
            val password = binding.passwordGroup//.editTextField.text.toString()

            if (isValidInputs(
                    email,
                    password,
                    companyName,
                    companyName.editTextField.text
                )
            ) {
                val companyLocation = companyLocation;
                firebaseAuth.createUserWithEmailAndPassword(
                    email.editTextField.text.toString(), password.editTextField.text.toString()
                )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val companyId = it.result.user?.uid!!
                            createCompany(
                                companyId,
                                companyName.editTextField.text.toString(),
                                email.editTextField.text.toString(),
                                companyLocation
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
        companyName: CustomInputFieldTextBinding,
        companyLocation: Editable
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
            fieldValidation(companyName, isStrValid),
            addressValidation(companyLocation)
        )

        return validations.all { it }

    }

    private fun addressValidation(input: Editable): Boolean {
        val isValid: Boolean
        if (input.isEmpty()) {
            Log.i("YourTag", "Address is empty")
            binding.addEditTextLine.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red
                )
            )
            binding.inputSuggestions.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red
                )
            )
            isValid = false
        } else {
            Log.i("YourTag", "Address is valid ")

            binding.addEditTextLine.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.Light_Gray
                )
            )
            binding.inputSuggestions.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.Dark_Gray
                )
            )
            isValid = true
        }
        return isValid
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

    private fun setHints() {
        setHintForEditText(R.id.email_group, null, R.string.email)
        setHintForEditText(R.id.password_group, null, R.string.password_title)
        setHintForEditText(R.id.company_name_group, null, R.string.company_name_title)
        setHintForEditText(R.id.password_group, null, R.string.password_title)
    }

    private fun createCompany(
        companyId: String,
        companyName: String,
        companyEmail: String,
        companyLocation: CompanyLocation
    ) {
        val database = Firebase.firestore
        val companyEntity = FirebaseCompany(companyName, "", companyEmail, companyLocation)

        val userType = UserType(Type.COMPANY)
        database.collection("usersType").document(companyId).set(userType).addOnSuccessListener {
            val companyRef = database.collection("companies").document(companyId)
            companyRef.set(companyEntity).addOnSuccessListener {
                companyRef.addSnapshotListener { snapshot, e ->
                    logCompanyNameFromDB(snapshot, e)
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_signUpCompanyFragment_to_signInFragment)
                }
            }.addOnFailureListener {
                Log.d("ERROR", "fail to create company to app")
            }
        }
    }

    // TODO remove
    private fun logCompanyNameFromDB(snapshot: DocumentSnapshot?, e: FirebaseFirestoreException?) {
        if (e != null) Log.d("ERROR", "Listen failed.", e)

        if (snapshot != null && snapshot.exists()) {
            val companyName = snapshot.getString("name")
            Log.d("SUCCESS", "Company Name: $companyName")
        } else Log.d("ERROR", "Company snapshot is null or does not exist.")
    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    fun searchPlaces(query: String) {
        context?.let {
            PlacesApiCall().getPlacesByQuery(it, query) { places ->
                placesSuggestions = places

                // Set places results as auto complete suggestions
                locationsAdapter.clear()
                places.forEach { locationsAdapter.add(it.title.plus(" - ").plus(it.address)) }
                locationsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setHintForEditText(
        editTextGroupId: Int,
        hintResourceId: Int?,
        inputTitleResourceId: Int?
    ) { // QUESTION - WHY DO WE NEED hintResourceId?
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
}
