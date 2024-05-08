package com.android.skillsync

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.android.skillsync.ViewModel.CompanyViewModel
import com.android.skillsync.ViewModel.UserAuthViewModel
import com.android.skillsync.databinding.CustomInputFieldTextBinding
import com.android.skillsync.databinding.FragmentEditCompanyProfileBinding
import com.android.skillsync.helpers.ActionBarHelper
import com.android.skillsync.helpers.DynamicTextHelper
import com.android.skillsync.helpers.ImageHelper
import com.android.skillsync.helpers.ImageUploadListener
import com.android.skillsync.helpers.ValidationHelper
import com.android.skillsync.models.Comapny.Company
import com.android.skillsync.models.CompanyLocation
import com.squareup.picasso.Picasso

class EditCompanyProfileFragment : Fragment() {
    private val userAuthViewModel: UserAuthViewModel by activityViewModels()
    private val compViewModel: CompanyViewModel by activityViewModels()

    private var _binding: FragmentEditCompanyProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var view: View
    private lateinit var saveBtn: Button
    private lateinit var companyViewModel: CompanyViewModel
    private lateinit var dynamicTextHelper: DynamicTextHelper
    private lateinit var email_address: String
    private lateinit var companyLocation: CompanyLocation

    private lateinit var profileImageUrl: String
    private lateinit var imageHelper: ImageHelper
    private lateinit var imageView: ImageView
    private  lateinit var loadingOverlay: LinearLayout

    private var profileImage: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditCompanyProfileBinding.inflate(layoutInflater, container, false)
        view = binding.root
        dynamicTextHelper = DynamicTextHelper(view)


        loadingOverlay = view.findViewById(R.id.edit_image_loading_overlay);
        loadingOverlay.visibility = View.INVISIBLE

        setHints()
        setUserData()
        setEventListeners()

        // Hide the BottomNavigationView
        ActionBarHelper.hideActionBarAndBottomNavigationView((requireActivity() as? AppCompatActivity))

        val backButton = view.findViewById<ImageView>(R.id.back_button)
       backButton.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_editCompanyProfileFragment_to_ProfileFragment)
        }

        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        ActionBarHelper.showActionBarAndBottomNavigationView(requireActivity() as? AppCompatActivity)
        super.onCreate(savedInstanceState)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setHints() {
        dynamicTextHelper.setTextViewText(R.id.company_name, R.string.company_name_title)
        dynamicTextHelper.setTextViewText(R.id.company_bio, R.string.bio_title)
    }
    private fun setEventListeners() {
        saveBtn = view.findViewById(R.id.save_group_btn)

        companyViewModel = CompanyViewModel()

        //allow update image
        imageView = view.findViewById(R.id.companyImage)

        imageHelper = ImageHelper(this, imageView, object : ImageUploadListener {
            override fun onImageUploaded(imageUrl: String) {
                loadingOverlay.visibility = View.INVISIBLE
            }
        })

        imageHelper.setImageViewClickListener {
            loadingOverlay.visibility = View.VISIBLE
        }

        saveBtn.setOnClickListener {
            val companyName = binding.companyName
            val companyBio = binding.companyBio

            if (isValidInputs(companyName,companyBio)) {
                val name = companyName.editTextField.text.toString()
                val bio = companyBio.editTextField.text.toString()

                val updatedCompany = Company(
                    id = userAuthViewModel.getUserId().toString(),
                    name = name,
                    location = companyLocation,
                    bio = bio,
                    email = email_address,
                    logo = imageHelper.getImageUrl() ?:profileImageUrl
                )

                companyViewModel.update(updatedCompany,updatedCompany.json,
                    onSuccessCallBack = {
                        Toast.makeText(context, "Company Details have been updated successfully", Toast.LENGTH_SHORT).show()
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
        companyName: CustomInputFieldTextBinding,
        bioGroup: CustomInputFieldTextBinding
    ): Boolean {
        val validationResults = mutableListOf<Boolean>()


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

        return validationResults.all { it }
    }

    private fun setUserData() {
        val userId = userAuthViewModel.getUserId().toString()
        compViewModel.getCompany(userId) { company ->
            binding.companyName.editTextField.setText(company.name)
            binding.companyBio.editTextField.setText(company.bio)
            email_address = company.email
            companyLocation = company.location
            profileImageUrl = company.logo


            profileImage = view.findViewById(R.id.companyImage)

            if (profileImage != null) {
                val profileImageUrl = if (company.logo.isEmpty())
                    "https://firebasestorage.googleapis.com/v0/b/skills-e4dc8.appspot.com/o/images%2FuserAvater.png?alt=media&token=1fa189ff-b5df-4b1a-8673-2f8e11638acc"
                else company.logo

                Picasso.get().load(profileImageUrl).into(profileImage)

            }

        }
    }
}
