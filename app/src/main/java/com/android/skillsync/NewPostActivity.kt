package com.android.skillsync

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class NewPostActivity : AppCompatActivity() {
    var emailGroup: TextView? = null
    var signOutBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        val intent = getIntent();
        val userEmail = intent.getStringExtra("userEmail")
        emailGroup = findViewById(R.id.email_group_post)
        emailGroup?.text = userEmail

        // log out - forget password
        signOutBtn = findViewById(R.id.sign_out)
        signOutBtn?.setOnClickListener {
            // forget user
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
