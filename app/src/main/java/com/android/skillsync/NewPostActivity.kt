package com.android.skillsync

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class NewPostActivity : AppCompatActivity() {
    var emailGroup: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        val intent = getIntent();
        val userEmail = intent.getStringExtra("userEmail")
        emailGroup = findViewById(R.id.email_group_post)
        emailGroup?.text = userEmail
    }
}
