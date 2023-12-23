package com.android.skillsync

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.skillsync.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignIn : AppCompatActivity() {
    var emailLayout: View? = null
    var passwordLayout: View? = null

    var emailLabel: TextView? = null
    var passwordLabel: TextView? = null

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // set up dynamic labels text fields
        emailLayout = findViewById(R.id.email_group)
        passwordLayout = findViewById(R.id.password_group)

        if (emailLayout != null && passwordLayout != null) {
            emailLabel = emailLayout?.findViewById(R.id.edit_text_label)
            passwordLabel = passwordLayout?.findViewById(R.id.edit_text_label)

            emailLabel!!.text = "Email"
            passwordLabel!!.text = "Password" // check
        }
        logIn()

    }

    private fun logIn(){
        binding  = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.logInButton.setOnClickListener{
            val email = binding.emailGroup.editTextField.text.toString()
            val password = binding.passwordGroup.editTextField.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty())
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                        val intent = Intent(this, NewPostActivity:: class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
