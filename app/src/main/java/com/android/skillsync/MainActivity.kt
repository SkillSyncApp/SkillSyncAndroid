package com.android.skillsync

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    var signInButton: Button? = null
    var signUpButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signInButton = findViewById(R.id.button_sign_in)
        signUpButton = findViewById(R.id.button_sign_up)

        signInButton?.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }

        signUpButton?.setOnClickListener {
            val intent = Intent(this, SignUpCompany::class.java)
            startActivity(intent)
        }
    }
}
