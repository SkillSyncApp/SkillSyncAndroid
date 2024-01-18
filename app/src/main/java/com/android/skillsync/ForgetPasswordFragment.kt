package com.android.skillsync

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.skillsync.databinding.FragmentForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordFragment : Fragment() {

    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var view: View
    private var backToSignIn: TextView? = null
    private var resetPassword: Button? = null
    private var email: TextView? = null
    private var emailLayout: View? = null

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentForgetPasswordBinding.inflate(layoutInflater, container, false)
        view = binding.root

        setEventListeners(view)

        return view
    }

    private fun resetPassword() {
        val emailGroup = view.findViewById<ConstraintLayout>(R.id.email_group)
        val email = emailGroup?.findViewById<TextView>(R.id.edit_text_field)?.text.toString()

        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    context?.applicationContext,
                    "We sent a password reset email to your email address",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Handle reset password failure
                Toast.makeText(
                    context?.applicationContext,
                    "Failed to send password reset email",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setEventListeners(view: View) {
        resetPassword = view.findViewById(R.id.reset_password)
        backToSignIn = view.findViewById(R.id.back_to_sign_in)
        emailLayout = view.findViewById(R.id.email_group)

        resetPassword?.setOnClickListener {
            resetPassword()
        }

        backToSignIn?.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_forgetPasswordFragment_to_signInFragment)
        }

        if (emailLayout != null) {
            email = emailLayout!!.findViewById(R.id.edit_text_field)

            resetPassword?.apply {
                isEnabled = false // initially disable the button
                alpha = 0.8f

                email?.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        isEnabled = s?.isNotEmpty() == true
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        resetPassword?.run {
                            isEnabled = s?.isNotEmpty() == true;
                            alpha = if (isEnabled) 1.0f else 0.8f // change opacity - disable button
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

