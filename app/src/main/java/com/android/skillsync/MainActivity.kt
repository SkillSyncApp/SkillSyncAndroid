package com.android.skillsync

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.android.skillsync.R

class MainActivity : AppCompatActivity() {

    var logInButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logInButton = findViewById(R.id.button_sign_in)
        logInButton?.setOnClickListener {
//          if you want to run a specific scree, change LogIn to your class
//          val intent = Intent(this, LogIn::class.java)
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
        }

    }
}
