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
import com.android.skillsync.models.CompanyLocation
import com.android.skillsync.models.Type
import com.android.skillsync.models.UserType
import com.android.skillsync.models.serper.Place
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
    private lateinit var placesSuggestions: Array<Place>

    private lateinit var companyLocation: CompanyLocation;

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding  = ActivitySignUpCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLocationsAutoComplete()

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signUpCompany.setOnClickListener{
            val companyName = binding.companyName.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val companyLocation = companyLocation;
            if((companyName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()))
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                if(it.isSuccessful){
                    val companyId = it.result.user?.uid!!
                    createCompany(companyId, companyName, companyLocation)
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
            val intent = Intent(this,NewPostActivity::class.java)
            intent.putExtra("userEmail", user.email)
            startActivity(intent)
            finish()
        }
    }

    private fun initLocationsAutoComplete() {
        val autoCompany: AutoCompleteTextView = findViewById(R.id.companySuggestion)

        locationsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ArrayList())

        autoCompany.setAdapter(locationsAdapter)
        autoCompany.setOnItemClickListener{ adapterView, view, i, l ->
            val selectedPlace = placesSuggestions[i];
            companyLocation = CompanyLocation(selectedPlace.address, selectedPlace.longitude, selectedPlace.latitude);
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

    private fun createCompany(companyId:String, companyName: String, companyLocation: CompanyLocation) {
        val database = Firebase.firestore
        val intent = Intent(this, SignIn::class.java)

        val companyEntity = Comapny(companyName, companyLocation);

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
    private fun logCompanyNameFromDB(snapshot: DocumentSnapshot?, e:  FirebaseFirestoreException?) {
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
            placesSuggestions = places

            // Set places results as auto complete suggestions
            locationsAdapter.clear()
            places?.forEach { locationsAdapter.add(it.title.plus(" - ").plus(it.address)) }
            locationsAdapter.notifyDataSetChanged()
        }
    }
}
