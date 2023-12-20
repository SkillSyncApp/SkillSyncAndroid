package com.android.skillsync

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.skillsync.R
import android.view.View
import android.widget.TextView
import android.util.Log;

class LogIn : AppCompatActivity() {

    var emailLayout: View? = null
    var passwordLayout: View? = null

    var emailLabel: TextView? = null
    var passwordLabel: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        // set up dynamic labels text fields
        emailLayout = findViewById(R.id.email_group)
        passwordLayout = findViewById(R.id.password_group)

        if (emailLayout != null && passwordLayout != null) {
            emailLabel = emailLayout?.findViewById(R.id.edit_text_label)
            passwordLabel = passwordLayout?.findViewById(R.id.edit_text_label)

            emailLabel!!.text = "Email"
            passwordLabel!!.text = "Password"
        }
    }
}
