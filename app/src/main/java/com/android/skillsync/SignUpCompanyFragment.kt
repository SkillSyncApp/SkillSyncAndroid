package com.android.skillsync

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.android.skillsync.Navigations.navigate
import com.android.skillsync.ViewModel.CompanyViewModel
import com.android.skillsync.ViewModel.UserAuthViewModel
import com.android.skillsync.databinding.CustomInputFieldPasswordBinding
import com.android.skillsync.databinding.CustomInputFieldTextBinding
import com.android.skillsync.databinding.FragmentSignUpCompanyBinding
import com.android.skillsync.helpers.DialogHelper
import com.android.skillsync.helpers.DynamicTextHelper
import com.android.skillsync.helpers.ImageHelper
import com.android.skillsync.helpers.ValidationHelper
import com.android.skillsync.models.Comapny.Company
import com.android.skillsync.models.CompanyLocation
import com.android.skillsync.models.serper.Place
import com.android.skillsync.services.PlacesApiCall
import com.firebase.geofire.core.GeoHash
import com.google.firebase.firestore.GeoPoint

class SignUpCompanyFragment : BaseFragment() {
    private lateinit var locationsAdapter: ArrayAdapter<String>
    private lateinit var placesSuggestions: ArrayList<Place>
    private lateinit var imageView: ImageView
    private lateinit var company: Company
    private lateinit var view: View
    private lateinit var signUpCompany: Button
    private lateinit var dynamicTextHelper: DynamicTextHelper
    private lateinit var companyViewModel: CompanyViewModel
    private lateinit var userAuthViewModel: UserAuthViewModel
    private lateinit var imageHelper: ImageHelper
    private lateinit var addressAutoComplete: AutoCompleteTextView

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
        userAuthViewModel = UserAuthViewModel()
        dynamicTextHelper = DynamicTextHelper(view)

        initLocationsAutoComplete()

        setHints()
        setEventListeners()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<ImageView>(R.id.back_button)


        backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initLocationsAutoComplete() {
        addressAutoComplete = view.findViewById(R.id.companySuggestion)

        locationsAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            ArrayList()
        )

        addressAutoComplete?.setAdapter(locationsAdapter)
        addressAutoComplete?.setOnItemClickListener { _, _, i, _ ->
            val selectedPlace = placesSuggestions[i];
            val latitude = selectedPlace.latitude.toDouble()
            val longitude = selectedPlace.longitude.toDouble()

            companyLocation = CompanyLocation(
                selectedPlace.address,
                GeoPoint(latitude, longitude),
                GeoHash(latitude, longitude)
            )
        }

        addressAutoComplete?.addTextChangedListener(object : TextWatcher {
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
        val dialogHelper = DialogHelper("Sorry", requireContext(), it)
        dialogHelper.showDialogMessage()
    }

    private val onSuccess: (String?) -> Unit = { userId ->
        userId?.let { id ->
            companyViewModel.addCompany(company.copy(id = id))
            Toast.makeText(requireContext(), "Account created successfully", Toast.LENGTH_SHORT).show()
            view.navigate(R.id.action_signUpCompanyFragment_to_signInFragment)
        }
    }

    private fun setEventListeners() {
        signUpCompany = view.findViewById(R.id.sign_up_company_btn)
        imageView = view.findViewById(R.id.logo_company)

        imageHelper = ImageHelper(this, imageView)
        imageHelper.setImageViewClickListener()

        signUpCompany.setOnClickListener {
            val companyNameGroup = binding.companyNameGroup
            val emailGroup = binding.emailCompany
            val bioGroup = binding.bioGroup
            val passwordGroup = binding.passwordCompany
            val address = binding.companySuggestion.text
            val logo = imageHelper.getImageUrl() ?:
            "https://firebasestorage.googleapis.com/v0/b/skills-e4dc8.appspot.com/o/images%2FuserAvater.png?alt=media&token=1fa189ff-b5df-4b1a-8673-2f8e11638acc"

            if (isValidInputs(emailGroup, passwordGroup, companyNameGroup, address,bioGroup)) {
                val email = emailGroup.editTextField.text.toString()
                val password = passwordGroup.editTextField.text.toString()
                val name = companyNameGroup.editTextField.text.toString()
                val bio = bioGroup.editTextField.text.toString()

                company = Company(
                    id = userAuthViewModel.getUserId().toString(),
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
        dynamicTextHelper.setTextViewText(R.id.email_company, R.string.email)
        dynamicTextHelper.setTextViewText(R.id.password_company, R.string.password)
        dynamicTextHelper.setTextViewText(R.id.bio_group, R.string.bio_title)
    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    fun searchPlaces(query: String) {
        context?.let {
            PlacesApiCall().getPlacesByQuery(it, query) { places ->

                locationsAdapter.clear();
                placesSuggestions = ArrayList();

                // Filter invalid addresses (with missing fields)
                val filteredPlacesArray = ArrayList<String>();
                places.forEach {
                    if (it.address != null && it.longitude != null && it.latitude != null) {
                        filteredPlacesArray.add(it.title.plus(" - ").plus(it.address))
                        placesSuggestions.add(it);
                    }
                }

//                Log.d("placesApi", filteredPlacesArray.size.toString())

                locationsAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    filteredPlacesArray
                )

                locationsAdapter.notifyDataSetChanged()
                addressAutoComplete?.setAdapter(locationsAdapter)
            }
        }
    }

    private fun isValidInputs(
        email: CustomInputFieldTextBinding,
        password: CustomInputFieldPasswordBinding,
        companyName: CustomInputFieldTextBinding,
        companyLocation: Editable,
        bioGroup: CustomInputFieldTextBinding
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
            ValidationHelper.isValidString(companyName.editTextField.text.toString())
                .also { isValid ->
                    ValidationHelper.handleValidationResult(isValid, companyName, requireContext())
                }
        )

        validationResults.add(
            ValidationHelper.isValidField(bioGroup.editTextField.text.toString())
                .also { isValid ->
                    ValidationHelper.handleValidationResult(isValid, bioGroup, requireContext())
                }
        )

        validationResults.add(
            ValidationHelper.isValidAddress(companyLocation)
                .also { isValid ->
                    ValidationHelper.handleValidationResult(
                        isValid,
                        binding.addEditTextLine,
                        binding.inputSuggestions,
                        requireContext()
                    )
                }
        )
        return validationResults.all { it }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
