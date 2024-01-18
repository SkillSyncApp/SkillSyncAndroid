package com.android.skillsync

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.skillsync.Navigations.navigate
import com.android.skillsync.databinding.FragmentSignInBinding
import com.android.skillsync.helpers.DialogHelper
import com.android.skillsync.repoistory.Auth.FireStoreAuthRepository

class SignInFragment : Fragment() {
    private var emailLayout: View? = null
    private var passwordLayout: View? = null

    private lateinit var view: View
    private var emailLabel: TextView? = null
    private var passwordLabel: TextView? = null
    private var forgetPassword: TextView? = null
    private var register: ConstraintLayout? = null

    private lateinit var binding: FragmentSignInBinding

    private val fireStoreAuthRepository: FireStoreAuthRepository = FireStoreAuthRepository()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        view = binding.root

        setUpDynamicTextFields()
        signInFirebase()
        setEventsListeners()

        return view
    }

    // TODO remember user

    private fun setEventsListeners() {
        forgetPassword = view.findViewById(R.id.forget_password_link)
        forgetPassword?.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_signInFragment_to_forgetPasswordFragment)
        }

        register = view.findViewById(R.id.register_group)
        register?.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_signInFragment_to_pickUserTypeFragment)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpDynamicTextFields() {
        emailLayout = view.findViewById(R.id.email_group)
        passwordLayout = view.findViewById(R.id.password_group)

        emailLabel = emailLayout?.findViewById(R.id.edit_text_label)
        passwordLabel = passwordLayout?.findViewById(R.id.edit_text_label)

        emailLabel!!.text = "Email"
        passwordLabel!!.text = "Password"
    }

    private fun signInFirebase() {
        binding.signInButton.setOnClickListener {

            val email = binding.emailGroup.editTextField.text.toString()
            val password = binding.passwordGroup.editTextField.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                fireStoreAuthRepository.signInUser(email, password, onSuccess, onError)
            }
        }
    }

    private val onSuccess: () -> Unit = {
        view.navigate(R.id.action_signInFragment_to_mapViewFragment)
    }

    private val onError: (String?) -> Unit = {
        val dialogHelper = DialogHelper(requireContext(), it)
        dialogHelper.showErrorDialog()
    }
}
