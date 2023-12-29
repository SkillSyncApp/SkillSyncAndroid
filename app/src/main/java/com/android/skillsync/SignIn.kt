package com.android.skillsync

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.skillsync.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignIn : AppCompatActivity() {
    private var emailLayout: View? = null
    private var passwordLayout: View? = null

    private var emailLabel: TextView? = null
    private var passwordLabel: TextView? = null
    private var forgetPassword: TextView? = null
    private var signUpView: ConstraintLayout? = null

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        setUpDynamicTextFields()
        signInFirebase()
        setEventsListeners()
    }

    // remember user
    override fun onStart() {
        super.onStart()
        val user = firebaseAuth.currentUser

        if(user != null) {
            val intent = Intent(this,NewPostActivity::class.java)
            intent.putExtra("userEmail", user.email)
            startActivity(intent)
            finish()
        }
    }
    
    private fun setEventsListeners() {
        forgetPassword = findViewById(R.id.forget_password_link)
        forgetPassword?.setOnClickListener {
            val intent = Intent(this, ForgetPassword::class.java)
            startActivity(intent)
        }

        signUpView = findViewById(R.id.register_group)
        signUpView?.setOnClickListener {
            val intent = Intent(this, SignUpCompany::class.java)
            startActivity(intent)
        }
    }

    private fun setUpDynamicTextFields() {
        emailLayout = findViewById(R.id.email_group)
        passwordLayout = findViewById(R.id.password_group)

        if (emailLayout != null && passwordLayout != null) {
            emailLabel = emailLayout!!.findViewById(R.id.edit_text_label)
            passwordLabel = passwordLayout!!.findViewById(R.id.edit_text_label)

            emailLabel!!.text = "Email"
            passwordLabel!!.text = "Password" // TODO fix bug
        }
    }

    private fun signInFirebase(){
        binding  = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signInButton.setOnClickListener{
            val email = binding.emailGroup.editTextField.text.toString()
            val password = binding.passwordGroup.editTextField.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty())
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                        val intent = Intent(this, NewPostActivity:: class.java)
                        intent.putExtra("userEmail", email)
                        startActivity(intent)
                    } else Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
        }
    }
}
