package com.android.skillsync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.skillsync.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth


class SignInFragment : Fragment() {
    private var emailLayout: View? = null
    private var passwordLayout: View? = null

    private lateinit var view: View
    private var emailLabel: TextView? = null
    private var passwordLabel: TextView? = null
    private var forgetPassword: TextView? = null
    private var register: ConstraintLayout? = null

    private lateinit var binding: FragmentSignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
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

    // remember user
    override fun onStart() {
        super.onStart()
        val user = firebaseAuth.currentUser

        if (user != null) {
        // Question - from sign up -> we navigate to sign in. Meaning user != null and then it will navigate to map.
        // Is that what we want?

         val navController = Navigation.findNavController(requireView())
          navController.navigate(R.id.action_signInFragment_to_mapViewFragment)
        }
    }

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

    private fun setUpDynamicTextFields() {
        emailLayout = view.findViewById(R.id.email_group)
        passwordLayout = view.findViewById(R.id.password_group)

        emailLabel = emailLayout?.findViewById(R.id.edit_text_label)
        passwordLabel = passwordLayout?.findViewById(R.id.edit_text_label)

        emailLabel!!.text = "Email"
        passwordLabel!!.text = "Password"
    }

    private fun signInFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()

        binding.signInButton.setOnClickListener {
            val email = binding.emailGroup.editTextField.text.toString()
            val password = binding.passwordGroup.editTextField.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Navigation.findNavController(view)
                            .navigate(R.id.action_signInFragment_to_mapViewFragment)
                    } else Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
