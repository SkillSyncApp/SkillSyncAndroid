package com.android.skillsync

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.skillsync.databinding.ActivityForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetPassword : AppCompatActivity() {

    lateinit var forgetPasswordBinding: ActivityForgetPasswordBinding
    private var backToSignIn: TextView? = null
    private var resetPassword: Button? = null
    private var email: TextView? = null
    private var emailLayout: View? = null


    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        forgetPasswordBinding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        val view = forgetPasswordBinding.root
        setContentView(view)

        addEventListeners()

        forgetPasswordBinding.resetPassword.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {
        val email = forgetPasswordBinding.emailGroup.editTextField.text.toString()

        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    applicationContext,
                    "We sent a password reset email to your email address",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                // Handle reset password failure
                Toast.makeText(
                    applicationContext,
                    "Failed to send password reset email",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun addEventListeners() {
        resetPassword = findViewById(R.id.reset_password)
        backToSignIn = findViewById(R.id.back_to_sign_in)
        emailLayout = findViewById(R.id.email_group)

        backToSignIn?.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
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
}
