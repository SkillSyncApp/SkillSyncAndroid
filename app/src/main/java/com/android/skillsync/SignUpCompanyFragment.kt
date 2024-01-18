package com.android.skillsync

import android.annotation.SuppressLint
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
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.skillsync.Navigations.navigate
import com.android.skillsync.ViewModel.CompanyViewModel
import com.android.skillsync.databinding.CustomInputFieldPasswordBinding
import com.android.skillsync.databinding.CustomInputFieldTextBinding
import com.android.skillsync.databinding.FragmentSignUpCompanyBinding
import com.android.skillsync.helpers.DialogHelper
import com.android.skillsync.helpers.DynamicTextHelper
import com.android.skillsync.helpers.ImageHelper
import com.android.skillsync.models.Comapny.Company
import com.android.skillsync.models.CompanyLocation
import com.android.skillsync.models.serper.Place
import com.android.skillsync.services.PlacesApiCall
import com.firebase.geofire.core.GeoHash
import com.google.firebase.firestore.GeoPoint

class SignUpCompanyFragment : Fragment() {
    private lateinit var locationsAdapter: ArrayAdapter<String>
    private lateinit var placesSuggestions: Array<Place>
    private lateinit var imageView: ImageView
    private lateinit var company: Company
    private lateinit var view: View
    private lateinit var signUpCompany: Button
    private lateinit var dynamicTextHelper: DynamicTextHelper
    private lateinit var companyViewModel: CompanyViewModel
    private lateinit var imageHelper: ImageHelper


    private var _binding: FragmentSignUpCompanyBinding? = null
    private val binding get() = _binding!!
    private var companyLocation: CompanyLocation = CompanyLocation(
        "Unknown",
        GeoPoint(0.0, 0.0),
        GeoHash(0.0, 0.0)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignUpCompanyBinding.inflate(layoutInflater, container, false)
        view = binding.root
        companyViewModel = CompanyViewModel()
        dynamicTextHelper = DynamicTextHelper(view)

        initLocationsAutoComplete()

        setHints()
        setEventListeners()

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initLocationsAutoComplete() {
        val autoCompany: AutoCompleteTextView? = view.findViewById(R.id.companySuggestion)

        locationsAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            ArrayList()
        )

        autoCompany?.setAdapter(locationsAdapter)
        autoCompany?.setOnItemClickListener { _, _, i, _ ->
            val selectedPlace = placesSuggestions[i];
            val latitude = selectedPlace.latitude.toDouble()
            val longitude = selectedPlace.longitude.toDouble()

            companyLocation = CompanyLocation(
                selectedPlace.address,
                GeoPoint(latitude, longitude),
                GeoHash(latitude, longitude)
            )
        }

        autoCompany?.addTextChangedListener(object : TextWatcher {
            @RequiresApi(Build.VERSION_CODES.O_MR1)
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    searchPlaces(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private val onError: (String?) -> Unit = {
        val dialogHelper = DialogHelper(requireContext(), it)
        dialogHelper.showErrorDialog()
    }

    private val onSuccess: () -> Unit = {
        companyViewModel.addCompany(company)
        view.navigate(R.id.action_signUpCompanyFragment_to_signInFragment)
    }

    private fun setEventListeners() {
        signUpCompany = view.findViewById(R.id.sign_up_company_btn)
        imageView = view.findViewById(R.id.logo_company)

        imageHelper = ImageHelper(this, imageView)
        imageHelper.setImageViewClickListener()

        signUpCompany.setOnClickListener {
            val companyNameGroup = binding.companyNameGroup
            val emailGroup = binding.emailGroup
            val bioGroup = binding.bioGroup
            val passwordGroup = binding.passwordGroup
            val address = binding.companySuggestion.text
            val logo = imageHelper.getImageUrl() ?: "DEFAULT LOGO" // TODO

            if (isValidInputs(emailGroup, passwordGroup, companyNameGroup, address)) {
                val email = emailGroup.editTextField.text.toString()
                val password = passwordGroup.editTextField.text.toString()
                val name = companyNameGroup.editTextField.text.toString()
                val bio = bioGroup.editTextField.text.toString()

                company = Company(
                    name = name,
                    email = email,
                    logo = logo,
                    location = companyLocation,
                    bio = bio
                )
                companyViewModel.createUserAsCompanyOwner(email, password, onSuccess, onError)
            }
        }
    }

    private fun setHints() {
        dynamicTextHelper.setTextViewText(R.id.company_name_group, R.string.company_name_title)
        dynamicTextHelper.setTextViewText(R.id.email_group, R.string.company_email_title)
        dynamicTextHelper.setTextViewText(R.id.password_group, R.string.password_title)
        dynamicTextHelper.setTextViewText(R.id.bio_group, R.string.bio_title)
    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    fun searchPlaces(query: String) {
        context?.let {
            PlacesApiCall().getPlacesByQuery(it, query) { places ->
                placesSuggestions = places

                locationsAdapter.clear()
                places.forEach { locationsAdapter.add(it.title.plus(" - ").plus(it.address)) }
                locationsAdapter.notifyDataSetChanged()
            }
        }
    }

    // TODO remove from fragment - validation helper
    private fun isValidInputs(
        email: CustomInputFieldTextBinding,
        password: CustomInputFieldPasswordBinding,
        companyName: CustomInputFieldTextBinding,
        companyLocation: Editable
    ): Boolean {
//        fieldErrorShown = false

        val isStrValid: (String) -> Boolean = { input ->
            val pattern = Regex("^[a-zA-Z\\. ]+\$")
            val isLengthValid = input.length >= 3
            pattern.matches(input) && isLengthValid
        }
        val isEmailValid: (String) -> Boolean = { email ->
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
        val isPassValid: (String) -> Boolean = { password ->
            /*TODO - do validations for password */

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
        /*TODO - do validations for password */
        inputGroup: CustomInputFieldPasswordBinding,
        validationCondition: (String) -> Boolean
    ): Boolean {
        val input = inputGroup.editTextField.text.toString()

        return if (input.isEmpty()) {
            //showBlankError(inputGroup)
            false
        } else {
            //  showTextError(inputGroup)//  showValidInput(inputGroup)
            validationCondition(input)
        }
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
        inputGroup.errorMessage.text = "Invalid " + inputGroup.editTextLabel.text
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
