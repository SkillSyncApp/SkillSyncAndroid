package com.android.skillsync

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.skillsync.databinding.ActivitySignUpCompanyBinding
import com.android.skillsync.models.Comapny
import com.android.skillsync.models.Type
import com.android.skillsync.models.UserType
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore

class SignUpCompany : AppCompatActivity() {

    private lateinit var binding:ActivitySignUpCompanyBinding
    private lateinit var firebaseAuth: FirebaseAuth
//    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding  = ActivitySignUpCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.signUpCompany.setOnClickListener{
            val companyName = binding.companyName.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if((companyName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()))
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                if(it.isSuccessful){
                    val companyId = it.result.user?.uid!!
                    createCompany(companyId, companyName)
                }else{
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun createCompany(companyId:String, companyName: String) {
        val database = Firebase.firestore
        val intent = Intent(this, SignIn:: class.java)

        val companyEntity = Comapny(companyName)

        val userType = UserType(Type.COMPANY)
        database.collection("usersType").document(companyId).set(userType).addOnSuccessListener {
            database.collection("companies").document(companyId).set(companyEntity).addOnSuccessListener {
                startActivity(intent) //TODO
            }
        }.addOnFailureListener{
            Log.d("failed", "fail")
        }
    }
}
