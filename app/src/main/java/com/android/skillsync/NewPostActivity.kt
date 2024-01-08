package com.android.skillsync

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
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

        // Set hints for input fields

        setHintForEditText(R.id.project_name_group, R.string.project_name_hint, R.string.project_name_title)
        setHintForEditText(R.id.project_description_group,  R.string.project_description_hint,null)
        setHintForEditText(R.id.project_deadline_group,null, R.string.project_deadline_title)
        setHintForEditText(R.id.project_availability_group,null, R.string.project__availability_title)

        // log out - forget password
        signOutBtn = findViewById(R.id.sign_out)
        signOutBtn?.setOnClickListener {
            // forget user
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
    private fun setHintForEditText(editTextGroupId: Int, hintResourceId: Int?, inputTitleResourceId: Int?) {
        val editTextGroup = findViewById<View>(editTextGroupId)
        val editTextLabel = editTextGroup.findViewById<TextView>(R.id.edit_text_label)
        val editTextField = editTextGroup.findViewById<EditText>(R.id.edit_text_field)

        inputTitleResourceId?.let {
            editTextLabel.text = getString(it)
        }

        hintResourceId?.let {
            editTextField.hint = getString(it)
        }
    }
}
