package com.android.skillsync

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.skillsync.databinding.ActivitySignUpCompanyBinding
import com.android.skillsync.models.Comapny
import com.android.skillsync.models.Type
import com.android.skillsync.models.UserType
import com.android.skillsync.services.PlacesApiCall
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore

class SignUpCompany : AppCompatActivity() {

    private lateinit var binding:ActivitySignUpCompanyBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var locationsAdapter: ArrayAdapter<String>

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding  = ActivitySignUpCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addAutoSuggestionsCompaniesList()

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signUpCompany.setOnClickListener{
            val companyName = binding.companySuggestion.text.toString()
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

    // remember user
    override fun onStart() {
        super.onStart()
        val user = firebaseAuth.currentUser

        if(user != null) {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("userEmail", user.email)
            startActivity(intent)
            finish()
        }
    }

    fun addAutoSuggestionsCompaniesList() {
        val autoCompany: AutoCompleteTextView = findViewById(R.id.companySuggestion)

        locationsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ArrayList())

        autoCompany.setAdapter(locationsAdapter)
        autoCompany.setOnItemClickListener{ adapterView, view, i, l ->
            Toast.makeText(this, adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show()
        }

        autoCompany.addTextChangedListener(object: TextWatcher {
            @RequiresApi(Build.VERSION_CODES.O_MR1)
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.isNotEmpty()) {
                        searchPlaces(s.toString())
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    fun createCompany(companyId:String, companyName: String) {
        val database = Firebase.firestore
        val intent = Intent(this, SignIn::class.java)

        val companyEntity = Comapny(companyName)

        val userType = UserType(Type.COMPANY)
        database.collection("usersType").document(companyId).set(userType).addOnSuccessListener {
            val companyRef = database.collection("companies").document(companyId)
            companyRef.set(companyEntity).addOnSuccessListener {
                companyRef.addSnapshotListener { snapshot, e ->
                    logCompanyNameFromDB(snapshot, e)
                    startActivity(intent) //maybe change it
                }
            }.addOnFailureListener {
                Log.d("ERROR", "fail to create company to app")
            }
        }
    }

    // TODO remove
    fun logCompanyNameFromDB(snapshot: DocumentSnapshot?, e:  FirebaseFirestoreException?) {
        if (e != null) Log.d("ERROR", "Listen failed.", e)

        if (snapshot != null && snapshot.exists()) {
            val companyName = snapshot.getString("name")
            Log.d("SUCCESS", "Company Name: $companyName")
        } else {
            Log.d("ERROR", "Company snapshot is null or does not exist.")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    fun searchPlaces(query: String) {
        PlacesApiCall().getPlacesByQuery(this, query) { places ->
            locationsAdapter.clear()
            places?.forEach { locationsAdapter.add(it.title.plus(" | ").plus(it.address)) }
            locationsAdapter.notifyDataSetChanged()
        }
    }
}
